/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import com.google.gson.JsonArray;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 *
 * @author An Nguyen
 */
public class APIReader {
    private ArrayList<DegreeProgram > degrees = new ArrayList<>();
    private final String degreeListAPI = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";
    private final String degreeDetailAPI = "https://sis-tuni.funidata.fi/kori/api/modules/";
    private final String studyModuleAPI = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=";
    private final String courseUnitAPI = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=";
    private final String identifierTUNI = "&universityId=tuni-university-root-id";
    
    // Purpose: get Json form the given API, type is "id" or "groupId" for taking out JsonObject properly;
    public JsonObject connectAPI(String link, String type){

        JsonObject JsonInformation = JsonParser.parseString("{}").getAsJsonObject();
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();
                if (type.equals("id")){

                    JsonInformation = JsonParser.parseString(String.valueOf(informationString)).getAsJsonObject();
                } else if (type.equals("groupId")){
                    JsonInformation = JsonParser.parseString(String.valueOf(informationString)).getAsJsonArray().get(0).getAsJsonObject();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return JsonInformation;
    }

    // Like set up in sisu: https://sis-tuni.funidata.fi/kori/api/modules/otm-df83fbbd-f82d-4fda-b819-78f6b2077fcb
    // "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=tut-sm-g-7094&universityId=tuni-university-root-id"
    // 5th degree
    public JsonArray takeRules(JsonObject rule) throws AnException{
        JsonArray rules = new JsonArray();
        String ruleType = rule.get("type").getAsString();
        if (ruleType.equals("CompositeRule")){
            // has "require"(min,max), "description"(en,fi), "allMandatory"
            JsonArray curr_rules = rule.get("rules").getAsJsonArray();

            for (int i=0; i<curr_rules.size(); i++){
                JsonObject curr_rule = curr_rules.get(i).getAsJsonObject();
                JsonArray sub_rules = takeRules(curr_rule);
                for (int j=0; j<sub_rules.size(); j++){
                    rules.add(sub_rules.get(j).getAsJsonObject());
                }
            }
        } else if (ruleType.equals("CreditsRule")){
            // has "credits" requirement
            rules = takeRules(rule.get("rule").getAsJsonObject());
        } else if (ruleType.equals("ModuleRule")){
            rules.add(rule);
        } else if (ruleType.equals("CourseUnitRule")){
            rules.add(rule);
        } else if (ruleType.equals("AnyModuleRule") || ruleType.equals("AnyCourseUnitRule")){
            // No groupID but means can have a search bar for any course/module
            rules.add(rule);
        } else {
            // nothing will be here
            throw new AnException("takeRules: Type unexpected: " + ruleType);

        }
        
        return rules;
    }
    
    // check the object not null and not JsonNull
    private boolean checkNotJsonNull(JsonObject obj, String entity){
        try{
            // check if the object itself is null
            if (obj.get(entity) == null){
                return false;
            }
            JsonNull test = obj.get(entity).getAsJsonNull();
            return false;
            
        } catch (Exception e) {
            return true;
        }
    }

    // Prioritize fi version, check if the content has en or fi and return it
    private String enOrFi(JsonObject content){
        if (checkNotJsonNull(content, "en")){
            return content.get("en").getAsString();
        } else {
            return content.get("fi").getAsString();
        }
    }

    //object can be studyModule or courseUnit(basic form) in Json basic form (in a rules, not its own JSON)
    private String makeAPIFromJson(JsonObject object) throws AnException{
        String type = object.get("type").getAsString();
        if (type.equals("ModuleRule")){
            return studyModuleAPI + object.get("moduleGroupId").getAsString() + identifierTUNI;
        } else if (type.equals("CourseUnitRule")){
            return courseUnitAPI + object.get("courseUnitGroupId").getAsString() + identifierTUNI;
        } else if (type.equals("AnyCourseUnitRule") || type.equals("AnyModuleRule")){
            return "Any Rule";
        } else {
            throw new AnException("makeAPIFromJson: Unexpected type: " + type);
        }
    }

    // Get detail information on the Degree or StudyModule
    private SubCompositeRule JsonToCompositeRule(JsonObject rule) throws AnException{
        String ruleType = rule.get("type").getAsString();
        if (ruleType.equals("CompositeRule") || ruleType.equals("CreditsRule")){
            Boolean isCredits = false;
            int minCredit = -1;
            int maxCredit = -1;
            int minRequire = -1;
            int maxRequire = -1;
            String description = null;
            Boolean allMandatory = false;
            if (ruleType.equals("CreditsRule")){
                if (checkNotJsonNull(rule, "credits")){
                    JsonObject credits = rule.get("credits").getAsJsonObject();
                    minCredit = credits.get("min").getAsInt();
                    if (checkNotJsonNull(credits, "max")){
                        maxCredit = credits.get("max").getAsInt();
                    } else {
                        maxCredit = minCredit;
                    }
                }
                isCredits = true;
                JsonArray subRules = rule.get("rule").getAsJsonObject().get("rules").getAsJsonArray();
                if (subRules.size() == 1){
                    if (subRules.get(0).getAsJsonObject().get("type").getAsString().equals("CompositeRule")){
                        rule = subRules.get(0).getAsJsonObject();
                    }
                } else {
                    rule = rule.get("rule").getAsJsonObject();
                }
            }
            
            if (checkNotJsonNull(rule, "require")){
                minRequire = rule.get("require").getAsJsonObject().get("min").getAsInt();
                if (checkNotJsonNull(rule.get("require").getAsJsonObject(),"max")){
                    maxRequire = rule.get("require").getAsJsonObject().get("max").getAsInt();
                } else {
                    maxRequire = rule.get("require").getAsJsonObject().get("min").getAsInt();
                }
            }
    
            if (checkNotJsonNull(rule, "description")){
                description = enOrFi(rule.get("description").getAsJsonObject());
            }
    
            if (checkNotJsonNull(rule, "allMandatory")){
                allMandatory = rule.get("allMandatory").getAsBoolean();
            }

            SubCompositeRule subCompositeRule = new SubCompositeRule(minRequire, maxRequire, description, allMandatory);

            if (isCredits){
                subCompositeRule.setMinCredit(minCredit);
                subCompositeRule.setMaxCredit(maxCredit);
            }

            JsonArray subCoursesJSON = rule.get("rules").getAsJsonArray();
            for (int j=0; j<subCoursesJSON.size(); j++){
                JsonObject currObject = subCoursesJSON.get(j).getAsJsonObject();
                String currObjectType = currObject.get("type").getAsString();
                if (currObjectType.equals("CompositeRule" )|| currObjectType.equals("CreditsRule")){
                    subCompositeRule.addSubComposite(JsonToCompositeRule(currObject));
                } else if (currObjectType.equals( "CourseUnitRule")){
                String courseUnitAPI = makeAPIFromJson(currObject);
                JsonObject courseUnitJson = connectAPI(courseUnitAPI, "groupId");
                CourseUnit courseUnit = JsonToCourseUnit(courseUnitJson);
                subCompositeRule.addCourse(courseUnit);
                } else if (currObjectType.equals("ModuleRule")){
                    String newStudyModuleAPI = makeAPIFromJson(currObject);
                    JsonObject newStudyModuleJson = connectAPI(newStudyModuleAPI, "groupId");
                    StudyModule newStudyModule = JsonToStudyModule(newStudyModuleJson);
                    subCompositeRule.addModule(newStudyModule);
                } else if (currObjectType.equals("AnyCourseUnitRule")){
                    subCompositeRule.addAnyRule(new AnyRule("AnyCourseUnitRule"));
                } else if (currObjectType.equals("AnyModuleRule")){
                    subCompositeRule.addAnyRule(new AnyRule("AnyModuleRule"));
                }
            }
            return subCompositeRule;
        } else {
            throw new AnException("JsonToCompositeRule: Unexpected Type: " + ruleType);
        }
    }
        
    // Give in the degreeprogram in Json and return the degreeprogram class.
    public DegreeProgram JsonToDegreeProgram(JsonObject degreeJson) throws AnException{

        JsonObject rule = degreeJson.get("rule").getAsJsonObject();
        String API = degreeDetailAPI + degreeJson.get("id").getAsString();
        String degreeName;
        if (checkNotJsonNull(degreeJson.get("name").getAsJsonObject(),"en")){
            degreeName = degreeJson.get("name").getAsJsonObject().get("en").getAsString();
        } else {
            degreeName = degreeJson.get("name").getAsJsonObject().get("fi").getAsString();
        }
        SubCompositeRule compositeRule = JsonToCompositeRule(rule);
        return new DegreeProgram(degreeName,
                                degreeJson.get("id").getAsString(),
                                degreeJson.get("groupId").getAsString(),
                                API,
                                compositeRule);

    }
    
    // Give in the studyModule in Json and return the StudyModule class
    public StudyModule JsonToStudyModule(JsonObject JsonStudyModule){
        String API = studyModuleAPI + JsonStudyModule.get("groupId").getAsString() + identifierTUNI;
        String studyModuleName = null;
        if ( checkNotJsonNull(JsonStudyModule, "name")){
            studyModuleName = enOrFi(JsonStudyModule.get("name").getAsJsonObject());
        }

        StudyModule studyModule = new StudyModule(studyModuleName,
                                                  JsonStudyModule.get("id").getAsString(),
                                                  JsonStudyModule.get("groupId").getAsString(),
                                                  API);
        return studyModule;
    }
    
    // Give in the CourseUnit in Json and return the CourseUnit class
    public CourseUnit JsonToCourseUnit(JsonObject courseUnitJson){
        String id = courseUnitJson.get("id").getAsString();
        // name have en or/and fi
        String name = null;
        if (checkNotJsonNull(courseUnitJson, "name")){
            name = enOrFi(courseUnitJson.get("name").getAsJsonObject());
        }
        String groupID = courseUnitJson.get("groupId").getAsString();
        int minCredit = courseUnitJson.get("credits").getAsJsonObject().get("min").getAsInt();
        int maxCredit = courseUnitJson.get("credits").getAsJsonObject().get("max").getAsInt();
        String API = courseUnitAPI + courseUnitJson.get("groupId").getAsString() + identifierTUNI;
        String content = null;
        if (checkNotJsonNull(courseUnitJson, "content")){
            content = enOrFi(courseUnitJson.get("content").getAsJsonObject());
        }
        String additional = null;
        if (checkNotJsonNull(courseUnitJson, "additional")){
            content = enOrFi(courseUnitJson.get("additional").getAsJsonObject());
        }
        JsonObject learningMaterial = new JsonObject();
        if (checkNotJsonNull(courseUnitJson, "learningMaterial")){
            learningMaterial = courseUnitJson.get("learningMaterial").getAsJsonObject();
        }
        JsonArray substitutions = courseUnitJson.get("substitutions").getAsJsonArray();
        JsonArray completionMethods = courseUnitJson.get("completionMethods").getAsJsonArray();
        String courseCode = courseUnitJson.get("code").getAsString();
        String gradeScaleId = courseUnitJson.get("gradeScaleId").getAsString();
        String outcomes = null;
        if (checkNotJsonNull(courseUnitJson, "outcomes")){
            content = enOrFi(courseUnitJson.get("outcomes").getAsJsonObject());
        }
        String prerequisites = null;
        if (checkNotJsonNull(courseUnitJson, "prerequisites")){
            prerequisites = enOrFi(courseUnitJson.get("prerequisites").getAsJsonObject());
        }
        JsonArray recommendedFormalPrerequisites = courseUnitJson.get("recommendedFormalPrerequisites").getAsJsonArray();
        JsonArray compulsoryFormalPrerequisites = courseUnitJson.get("compulsoryFormalPrerequisites").getAsJsonArray();
        JsonArray responsibilityInfos = courseUnitJson.get("responsibilityInfos").getAsJsonArray();
        JsonArray possibleAttainmentLanguages = courseUnitJson.get("possibleAttainmentLanguages").getAsJsonArray();
        JsonArray studyFields = courseUnitJson.get("studyFields").getAsJsonArray();
        JsonArray curriculumPeriodIds = courseUnitJson.get("curriculumPeriodIds").getAsJsonArray();
        String inclusionApplicationInstruction = null;
        if (checkNotJsonNull(courseUnitJson, "inclusionApplicationInstruction")){
            inclusionApplicationInstruction = enOrFi(courseUnitJson.get("inclusionApplicationInstruction").getAsJsonObject());
        }
        return new CourseUnit(name, id, groupID, minCredit, maxCredit, API, content, additional, learningMaterial, substitutions, completionMethods, courseCode, gradeScaleId, outcomes, prerequisites, recommendedFormalPrerequisites, compulsoryFormalPrerequisites, studyFields, responsibilityInfos, possibleAttainmentLanguages, curriculumPeriodIds, inclusionApplicationInstruction);
    }

    
    // Take in studyModule in Json form and studyModule class form to create the structure below it.
    public void onClickStudyModule(StudyModule studyModule) throws AnException{
        String studyModuleAPI = studyModule.getAPI();
        JsonObject studyModuleJSON = connectAPI(studyModuleAPI, "groupId");
        //System.out.println("Clicking");
        JsonObject rule = studyModuleJSON.get("rule").getAsJsonObject();
        String ruleType = rule.get("type").getAsString();
        if (ruleType.equals("CompositeRule") || ruleType.equals("CreditsRule")){
            SubCompositeRule subCompositeRule = JsonToCompositeRule(rule);
            studyModule.setCompositeRule(subCompositeRule);

        } else {
            throw new AnException("onClickStudyModule: Unexpected Type: " + ruleType);

        }
    }
    
    // call API containing all degree and return JsonArray contains all degrees.
    public JsonArray callAllDegrees(){
        return connectAPI(degreeListAPI, "id").get("searchResults").getAsJsonArray();
    }
    
    
    // Example of a main function call
    public void mainLookLike() throws AnException{
        //----------call API degree list -------------------------------------
        JsonArray degreeArray = callAllDegrees();
        // SISU: Take the Bachelor of Science and Engineering degree
        JsonObject degreeOverview = degreeArray.get(8).getAsJsonObject();
        // create the API to call that specific degree
        String currentDegreeAPI = degreeDetailAPI + degreeOverview.get("id").getAsString();

        // -------------- and added to degrees array -----------------
        

        //System.out.println(degrees.size());
        //---------------Call API 1 degree detail --------------------------------
        JsonObject degreeDetail = connectAPI(currentDegreeAPI, "id");
        // Make the degree class and add it to the degrees list up in the beginning of this file.
        DegreeProgram degree = JsonToDegreeProgram(degreeDetail);
        degrees.add(degree);
        JsonArray degreeRules = takeRules(degreeDetail.get("rule").getAsJsonObject());

        // ---------------- take submodules/courses from degree detail----------------

        // call out 1 module (usually suppose to be only 1) ----------------------------
        // SISU: Get Natural Sciences and Mathematics path
        JsonObject studyModuleOverview = degreeRules.get(0).getAsJsonObject();
        String currentStudyModuleAPI = studyModuleAPI + studyModuleOverview.get("moduleGroupId").getAsString() + identifierTUNI;
        // contain: submodule or course
        JsonObject studyModuleDetail = connectAPI(currentStudyModuleAPI, "groupId");
        StudyModule firStudyModule = JsonToStudyModule(studyModuleDetail);
        // User click on the studyModule to see more details
        onClickStudyModule(firStudyModule);
        // Look at the Basic Studies in Natural Sciences in Natural Sciences and Mathematics
        String basic_studies_api = firStudyModule.getCompositeRule().getSubModules().get(1).getAPI();
        JsonObject basic_studies_obj = connectAPI(basic_studies_api, "groupId");
        StudyModule basic_studies_module = firStudyModule.getCompositeRule().getSubModules().get(1);
        // When user click on it
        onClickStudyModule(basic_studies_module);

    }

}
