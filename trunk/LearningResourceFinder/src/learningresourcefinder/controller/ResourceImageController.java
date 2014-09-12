package learningresourcefinder.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import learningresourcefinder.model.Resource;
import learningresourcefinder.model.User;
import learningresourcefinder.repository.ResourceRepository;
import learningresourcefinder.security.SecurityContext;
import learningresourcefinder.service.ResourceService;
import learningresourcefinder.service.UserService;
import learningresourcefinder.util.CurrentEnvironment;
import learningresourcefinder.util.FileUtil;
import learningresourcefinder.util.ImageUtil;
import learningresourcefinder.util.NotificationUtil;
import learningresourcefinder.web.UrlUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/resource")
public class ResourceImageController extends BaseController<User> {

	@Autowired ResourceService resourceService;
	@Autowired ResourceRepository resourceRepository;
	@Autowired UserService userService;
	@Autowired CurrentEnvironment currentEnvironment;
	
	@RequestMapping("/image")  // FIXME Is this method still used? John 2013-12
	public ModelAndView resourceImage(@RequestParam("id") long userid){
		User user = getRequiredEntity(userid);
		ModelAndView mv= new ModelAndView("resourceimage", "user", user);
		
		return mv;
	}

	@RequestMapping("/imageadd")
	public ModelAndView resourceImageAdd(@RequestParam("idResource") long resourceid, @RequestParam("file") MultipartFile multipartFile) throws Exception{
		
		Resource resource = resourceRepository.find(resourceid);
		SecurityContext.assertCurrentUserMayEditThisResource(resource);
		
		BufferedImage image = null;
		image = ImageUtil.readImage(multipartFile.getBytes());
		
		ImageUtil.createOriginalAndScalesImageFileForResource(resource, image, currentEnvironment);
		
		ModelAndView mv = new ModelAndView("redirect:"+UrlUtil.getRelativeUrlToResourceDisplay(resource));

		return mv;
	}


	@RequestMapping("/imageaddUrl" )
	public ModelAndView resourceImageAddUrl(@RequestParam("idResource") long resourceid, @RequestParam("strUrl") String url) throws Exception {
		
		Resource resource = resourceRepository.find(resourceid);
		SecurityContext.assertCurrentUserMayEditThisResource(resource);
		
		ModelAndView mv = new ModelAndView("redirect:"+UrlUtil.getRelativeUrlToResourceDisplay(resource));

		BufferedImage image = null;
		
		try {
            image = ImageUtil.readImage(url);
        } catch (RuntimeException e) {
        	NotificationUtil.addNotificationMessage("veuillez indiquer une URL valide");
            return mv;//useless to try to save image if we don't have it
        }
		
		ImageUtil.createOriginalAndScalesImageFileForResource(resource, image, currentEnvironment);

        return mv;
	}

    
    
	@RequestMapping("/change")
	public ModelAndView resourceImageChange(@RequestBody ArrayList<String> order){	

		String idResource = ((String) (order.toArray()[0])).split("-")[0];
		Resource resource = resourceRepository.find(Long.parseLong(idResource));
		resourceService.changeGalleryOrder(order, resource);
		
		//Resource resource = resourceRepository.find(Long.parseLong(order[0].split("-")[0]));
		
		//resourceService.changeGalleryOrder(arrayList, resource);
		
		return new ModelAndView("redirect:"+UrlUtil.getRelativeUrlToResourceDisplay(resource));
	 }
	
	 @RequestMapping("/delete")
	 public ModelAndView resourceImageDelete(@RequestParam("id") long resrouceid, @RequestParam("img") long imgid){	
		 Resource resource = resourceRepository.find(resrouceid);
		 SecurityContext.assertCurrentUserMayEditThisResource(resource);
		 
		 resourceService.resourceImageDelete(resource, imgid);
		 return new ModelAndView("redirect:"+UrlUtil.getRelativeUrlToResourceDisplay(resource));
	 }
	 
	@RequestMapping("/ajax/checkClipBoard")
	public @ResponseBody String checkClipBoard(@RequestParam(value = "id") long id) throws Exception {
     
        Resource resource = resourceRepository.find(id);
        SecurityContext.assertCurrentUserMayEditThisResource(resource);
        
        BufferedImage image = null;
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        // If clipboard contains an image
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                
                image = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);

                // Save this image with prefix "tmp-"
                String path = FileUtil.getGenFolderPath(currentEnvironment) + FileUtil.RESOURCE_SUB_FOLDER + FileUtil.RESOURCE_ORIGINAL_SUB_FOLDER;
                String fileName = "tmp-" + resource.getId() + "-" + (resource.getNumberImage() + 1) + ".png";
                
                File imageTemp = new File(path, fileName);
                FileUtil.ensureFolderExists(path);
                ImageIO.write(image, "png", imageTemp);
                
                return fileName;
                
            } catch (UnsupportedFlavorException | IOException e) {
                throw new RuntimeException(e);
            }
            
        } else {
            NotificationUtil.addNotificationMessage("Veuillez effectuer une capture d'écran");
            return null;
        }
    }

    @RequestMapping("/ajax/imageaddPrintScreen")
    public ModelAndView cropTheImage( 
            @RequestParam(value = "xCoord") String x,
            @RequestParam(value = "yCoord") String y,
            @RequestParam(value = "wCoord") String w,
            @RequestParam(value = "hCoord") String h,
            @RequestParam(value = "imageFileName") String imageFileName,
            @RequestParam(value = "resourceid") long id
            ) throws IOException {
  
        Resource resource = resourceRepository.find(id);
        SecurityContext.assertCurrentUserMayEditThisResource(resource);

        String path = FileUtil.getGenFolderPath(currentEnvironment) + FileUtil.RESOURCE_SUB_FOLDER + FileUtil.RESOURCE_ORIGINAL_SUB_FOLDER;
        
        File tmpFile = new File(path, imageFileName);
        
        BufferedImage outImage = ImageIO.read(tmpFile);
        BufferedImage cropImage;
        
        // Check the inputImage to fill the transparent pixels in PNG and replace to white pixels
        // Without that, usually, in the Mac's computers the image is black or pink. 
        if( outImage.getColorModel().getTransparency() != Transparency.OPAQUE) {
            outImage = fillTransparentPixels(outImage, Color.WHITE);
        }

        if (x != null && y != null && w != null && h != null) {
            int cropx = (int) Double.parseDouble(x);
            int cropy = (int) Double.parseDouble(y);
            int cropw = (int) Double.parseDouble(w);
            int croph = (int) Double.parseDouble(h);

            cropImage = outImage.getSubimage(cropx, cropy, cropw, croph);
        }
        else
            cropImage = outImage;
       
        String newImageName = imageFileName.substring(4);
        
        FileUtil.ensureFolderExists(path);
        ImageIO.write(cropImage, "jpg", new File(path, newImageName));
        
        ImageUtil.createOriginalAndScalesImageFileForResource(resource, cropImage, currentEnvironment);
        
        // Delete temporary file
        if (tmpFile.exists()) tmpFile.delete();

        return new ModelAndView("redirect:"+UrlUtil.getRelativeUrlToResourceDisplay(resource));
    }
    
    
    public static BufferedImage fillTransparentPixels(BufferedImage image, Color fillColor) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image2.createGraphics();
        g.setColor(fillColor);
        g.fillRect(0, 0, w, h);
        g.drawRenderedImage(image, null);
        g.dispose();
        return image2;
    }
    
    @RequestMapping("/ajax/deleteTempImage")
    public ModelAndView deleteTempImage( 
            @RequestParam(value = "imageFileName") String imageFileName,
            @RequestParam(value = "resourceid") long id)  {

        Resource resource = resourceRepository.find(id);
        SecurityContext.assertCurrentUserMayEditThisResource(resource);

        String path = FileUtil.getGenFolderPath(currentEnvironment) + FileUtil.RESOURCE_SUB_FOLDER + FileUtil.RESOURCE_ORIGINAL_SUB_FOLDER;
        File tmpFile = new File(path, imageFileName);
        
        // Delete temporary file
        if (tmpFile.exists()) tmpFile.delete();

        return new ModelAndView("redirect:"+UrlUtil.getRelativeUrlToResourceDisplay(resource));
    }

    @RequestMapping("/displayPrintScreenHelpText")
    public ModelAndView displayPrintScreenHelpText(@RequestParam(value = "resourceid") long id) {
  
        Resource resource = resourceRepository.find(id);
        SecurityContext.assertCurrentUserMayEditThisResource(resource);

        return new ModelAndView("resourceimageprintscreenhelp", "resource", resource);
    }

}