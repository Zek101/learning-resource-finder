package learningresourcefinder.service.crawler;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import learningresourcefinder.model.Cycle;
import learningresourcefinder.model.Resource;
import learningresourcefinder.model.UrlResource;
import learningresourcefinder.model.Resource.Topic;
import learningresourcefinder.repository.CycleRepository;
import learningresourcefinder.repository.ResourceRepository;
import learningresourcefinder.repository.UrlResourceRepository;
import learningresourcefinder.search.SearchOptions.Format;
import learningresourcefinder.search.SearchOptions.Language;
import learningresourcefinder.search.SearchOptions.Platform;
import learningresourcefinder.security.SecurityContext;
import learningresourcefinder.web.UrlUtil;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CrawlerService
{	
    @Autowired
    CycleRepository cycleRepository;
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    UrlResourceRepository urlResourceRepository;    


    public void crawlerPage(String pageName) throws IOException 
    {
        pageName = pageName.toLowerCase();
        switch (pageName) 
        {
        case "brainpop" :
            CrawlerBrainPOP.crawler(this);  break;      
        case "championmath" :
            CrawlerChampionMath.crawler(this); break; 
        case "classeprimaire" :
            CrawlerClassePrimaire.crawler(this); break;
        case "exofiche" :
            CrawlerExoFiche.crawler(this); break;
        case "fondationlamap" :
            CrawlerFondationLamap.crawler(this); break;
        case "khanacademy" :
            CrawlerKhanAcademy.crawler(this); break;   
        case "crawlerlarecre" :
            CrawlerLaRecre.crawler(this); break;
        case "professeurphifix" :
            CrawlerProfesseurPhifix.crawler(this); break;
        case "soutien67" :
            CrawlerSoutien67.crawler(this); break;
        case "toutsavoir" :
            CrawlerToutSavoir.crawler(this); break;
        }
    }
    
    public void persistRessource(String name, String url, String topic, String description,int duration,String minCycle,String maxCycle ) 
    {
        Resource r = urlResourceRepository.getFirstResourceWithSimilarUrl(url);
        if (r == null)
        {
            r = new Resource(getSubString(name, 50),description,SecurityContext.getUser());
            r.setLanguage(Language.FR);
            r.setTopic(getTopicFromString(topic));
            r.setFormat(Format.INTERACTIVE);
            Set<Platform> tempSet = new HashSet<>();
            tempSet.add(Platform.BROWSER);
            r.setPlatforms(tempSet);
            UrlResource urlResource = new UrlResource(name,url,r);
            if(duration!=0)
            {
                r.setDuration(duration);
            }

            Cycle cycle =  getCycle(maxCycle);
            if(cycle!=null)
            {
                r.setMaxCycle(cycleRepository.findByName(maxCycle));          
            }
            cycle =  getCycle(minCycle);
            if(cycle!=null)
            {            
                r.setMinCycle(cycleRepository.findByName(minCycle));        
            }
            resourceRepository.persist(r);
            urlResourceRepository.persist(urlResource);
            r.getShortId();    
        } 
        else 
        {
            Cycle cycle =  getCycle(maxCycle);
            if(cycle!=null)
            {
                if(getCycleNumber(cycle)>=0 && getCycleNumber(cycle)>getCycleNumber(r.getMaxCycle()))
                    r.setMaxCycle(cycle);          
            }
            cycle =  getCycle(minCycle);
            if(cycle!=null)
            {        
                if(getCycleNumber(cycle)>=0 && getCycleNumber(cycle)<getCycleNumber(r.getMaxCycle()))                     
                    r.setMinCycle(cycle);        
            }                            
        }
    }

    public static String getSubString(String s,int i)
    {
        return (s.length()<=i)?s:s.substring(0, i);
    }

    public static Topic getTopicFromString(String s)
    {
        s = s.toLowerCase();
        switch(s)
        {
        case("grammaire") : case("lecture") : case("conugaison") : 
        case("conjugaison")  : case("orthographe") : case("savoir-ecouter")  :
        case("vocabulaire")  : case("coach-memorisation") :
            return Topic.FRENCH;
        case("geographie")  : case("géographie")  : case("geographique")  : 
        case("géographique") :
            return Topic.GEO;
        case("math")  : case("nombres")  : case("opération") :
            return Topic.MATH;
        case("histoire")  : case("historique"):
            return Topic.HISTORY;
        case("scientifique")  : case("science") :
            return Topic.SCIENCE;
        default : return null;
        }
    }

    private Cycle getCycle(String cycleName) 
    {
        cycleName = cycleName.toUpperCase();
        switch(cycleName)
        {
        case("CP") : case("CE1") :case("P1-2") : 
            return cycleRepository.findByName("P1-2"); 
        case("CE2") : case("CM1") :case("P3-4") : 
            return cycleRepository.findByName("P3-4"); 
        case("CM2") : case("6") :case("P5-6") : 
            return cycleRepository.findByName("P5-6"); 
        default : return null;        
        }
    }
    
    private int getCycleNumber(Cycle cycle) 
    {
        String cycleName = cycle.getName().toUpperCase();
        switch(cycleName)
        {
        case("P1-2") : 
            return 0; 
        case("P3-4") : 
            return 1; 
        case("P5-6") : 
            return 2; 
        default : return -1;        
        }
    }
}