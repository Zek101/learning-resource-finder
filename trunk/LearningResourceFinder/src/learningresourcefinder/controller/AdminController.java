package learningresourcefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import learningresourcefinder.batch.ImportCompetencesFromVraiForumBatch;
import learningresourcefinder.model.User;
import learningresourcefinder.model.User.Role;
import learningresourcefinder.repository.BaseRepository;
import learningresourcefinder.security.SecurityContext;
import learningresourcefinder.service.IndexManagerService;
import learningresourcefinder.util.NotificationUtil;
import learningresourcefinder.util.NotificationUtil.Status;

@Controller
public class AdminController extends BaseRepository<User> {
	
	@Autowired IndexManagerService indexManagerService;
	@Autowired ImportCompetencesFromVraiForumBatch importCompetencesFromVraiForumBatch;
   
	@RequestMapping("/admin")
	public String admin() {
	    SecurityContext.assertUserHasRole(Role.ADMIN);
		return "admin";
    }
    
    @RequestMapping("/createindex")
    public String createIndex(){
        SecurityContext.assertUserHasRole(Role.ADMIN);
    	indexManagerService.removeIndexes();
    	indexManagerService.createIndexes();
		NotificationUtil.addNotificationMessage("Inexes supprimés puis recréés avec succcès", Status.SUCCESS);
		return "admin";  // JSP
    	
    }
    
    @RequestMapping("/importfromvraisforum")
    public String importFromVraisForum() {
        importCompetencesFromVraiForumBatch.run();
        NotificationUtil.addNotificationMessage("Import réussi");
        return "admin";
    }
    
}