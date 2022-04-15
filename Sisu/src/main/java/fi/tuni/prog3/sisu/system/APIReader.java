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
    // Purpose: for taking all submodules and courses in rule and return jsonarray containing them.
    public JsonArray takeRules(JsonObject rule){
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
            rules = takeRules(rule.get("rule").getAsJsonObject());
        } else if (ruleType.equals("ModuleRule")){
            rules.add(rule);
        } else if (ruleType.equals("CourseUnitRule")){
            rules.add(rule);
        } else if (ruleType.equals("AnyModuleRule") || ruleType.equals("AnyCourseUnitRule")){
            // No groupID but means can have a search bar for any course/module
        } else {
            // nothing will be here
            System.out.println("Special case happened, which shouldn't be happening!!!!");
        }     
        return rules;
    }
    
        public static boolean checkNotJsonNull(JsonObject obj, String entity){
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
        
    // Give in the degreeprogram in Json and return the degreeprogram class
    public DegreeProgram JsonToDegreeProgram(JsonObject degreeJson){
        JsonObject rule = degreeJson.get("rule").getAsJsonObject();
        String API = degreeDetailAPI + degreeJson.get("id").getAsString();
        String degreeName;
        if (checkNotJsonNull(degreeJson.get("name").getAsJsonObject(),"en")){
            degreeName = degreeJson.get("name").getAsJsonObject().get("en").getAsString();
        } else {
            degreeName = degreeJson.get("name").getAsJsonObject().get("fi").getAsString();
        }
        if (rule.get("type").getAsString().equals("CompositeRule")){
            int maxRequire = -1;
            int minRequire = -1;
            String desString = null;   
            Boolean allMandatory = false;        
            if (checkNotJsonNull(rule, "require")){
                    minRequire = rule.get("require").getAsJsonObject().get("min").getAsInt();
                if (checkNotJsonNull(rule.get("require").getAsJsonObject(),"max")){
                    maxRequire = rule.get("require").getAsJsonObject().get("max").getAsInt();
                } else {
                    maxRequire = rule.get("require").getAsJsonObject().get("min").getAsInt();
                }
            }
    
            if (checkNotJsonNull(rule, "description")){
                desString = rule.get("description").getAsString();
            }

            if (checkNotJsonNull(rule, "allMandatory")){
                allMandatory = rule.get("allMandatory").getAsBoolean();
            }
            return new DegreeProgram(degreeName,
            degreeJson.get("id").getAsString(),
            degreeJson.get("groupId").getAsString(),
            API,
            minRequire,
            maxRequire,
            desString,
            allMandatory);

            
        } else { // (rule.get("type").getAsString().equals("CreditsRule"))
            int minCredit = -1;
            int maxCredit = -1;
            if (checkNotJsonNull(rule, "credits")){
                JsonObject credits = rule.get("credits").getAsJsonObject();
                minCredit = credits.get("min").getAsInt();
                if (checkNotJsonNull(credits, "max")){
                    maxCredit = credits.get("max").getAsInt();
                } else {
                    maxCredit = minCredit;
                }
            }
            return new DegreeProgram(degreeName,
            degreeJson.get("id").getAsString(),
            degreeJson.get("groupId").getAsString(),
            API,
            minCredit,
            maxCredit);

        }
   }
    
    // Give in the studyModule in Json and return the StudyModule class
    public StudyModule JsonToStudyModule(JsonObject JsonStudyModule){
        JsonObject rule = JsonStudyModule.get("rule").getAsJsonObject();
        String API = studyModuleAPI + JsonStudyModule.get("groupId").getAsString() + identifierTUNI;
        String studyModuleName = null;
        if ( checkNotJsonNull(JsonStudyModule, "name")){
            studyModuleName = enOrFi(JsonStudyModule.get("name").getAsJsonObject());
        }
        if (rule.get("type").getAsString().equals("CompositeRule")){
            int maxRequire = -1;
            int minRequire = -1;
            String desString = null;   
            Boolean allMandatory = false;        
            if (checkNotJsonNull(rule, "require")){
                    minRequire = rule.get("require").getAsJsonObject().get("min").getAsInt();
                if (checkNotJsonNull(rule.get("require").getAsJsonObject(),"max")){
                    maxRequire = rule.get("require").getAsJsonObject().get("max").getAsInt();
                } else {
                    maxRequire = rule.get("require").getAsJsonObject().get("min").getAsInt();
                }
            }
    
            if (checkNotJsonNull(rule, "outcomes")){
                System.out.println(rule.get("outcomes"));
                desString = enOrFi(rule.get("outcomes").getAsJsonObject());
            }

            if (checkNotJsonNull(rule, "allMandatory")){
                allMandatory = rule.get("allMandatory").getAsBoolean();
            }

            return new StudyModule(studyModuleName,
            JsonStudyModule.get("id").getAsString(),
            JsonStudyModule.get("groupId").getAsString(),
            API,
            minRequire,
            maxRequire,
            desString,
            allMandatory);

        } else {
            int minCredit = -1;
            int maxCredit = -1;
            if (checkNotJsonNull(rule, "credits")){
                JsonObject credits = rule.get("credits").getAsJsonObject();
                minCredit = credits.get("min").getAsInt();
                if (checkNotJsonNull(credits, "max")){
                    maxCredit = credits.get("max").getAsInt();
                } else {
                    maxCredit = minCredit;
                }
            }
            return new StudyModule(studyModuleName,
                JsonStudyModule.get("id").getAsString(),
                JsonStudyModule.get("groupId").getAsString(),
                API,
                minCredit,
                maxCredit);
        }

    }
    
    // Give in the CourseUnit in Json and return the CourseUnit class
    public CourseUnit JsonToCourseUnit(JsonObject courseUnitJson){
        String id = courseUnitJson.get("id").getAsString();
        // name have en or/and fi
        String name = null;
        if (checkNotJsonNull(courseUnitJson, name)){
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
    
        
    // Prioritize fi version, check if the content has en or fi and return it
    public String enOrFi(JsonObject content){
        if (checkNotJsonNull(content, "fi")){
            return content.get("fi").getAsString();
        } else {
            return content.get("en").getAsString();
        }
    }
    
    //object can be studyModule or courseUnit in a StudyModule rule.
    public String makeAPIFromJson(JsonObject object){
        String type = object.get("type").getAsString();
        if (type.equals("ModuleRule")){
            return studyModuleAPI + object.get("moduleGroupId").getAsString() + identifierTUNI;
        } else if (type.equals("CousreUnitRule")){
            return courseUnitAPI + object.get("courseUnitGroupId").getAsString() + identifierTUNI;
        }
        return "Any Rule";
    }
    
    // Take in studyModule in Json form and studyModule class form to create the structure below it.
    public void studyModuleRecursive(JsonObject studyModuleJson, StudyModule studyModule){
        JsonArray rules = takeRules(studyModuleJson.get("rule").getAsJsonObject());
        JsonArray modules = new JsonArray();
        for (int i=0; i<rules.size(); i++){
            JsonObject curr_obj = rules.get(i).getAsJsonObject();
            String curr_obj_type = curr_obj.get("type").getAsString();
            if (curr_obj_type.equals("ModuleRule")){
                modules.add(curr_obj);
            } else if (curr_obj_type.equals("CourseUnitRule")){
                String courseUnitAPI = makeAPIFromJson(curr_obj);
                JsonObject courseUnitJson = connectAPI(courseUnitAPI, "groupId");
                CourseUnit courseUnit = JsonToCourseUnit(courseUnitJson);
                studyModule.addSubCourse(courseUnit);
            } else if (curr_obj_type.equals("AnyCourseUnitRule")){
                studyModule.addAnyRule(new AnyRule("AnyCourseUnitRule"));
            } else if (curr_obj_type.equals("AnyModuleRule")){
                studyModule.addAnyRule(new AnyRule("AnyModuleRule"));
            }
        }
        if (modules.size()!=0){
            for (int i=0; i<modules.size(); i++){
                String newStudyModuleAPI = makeAPIFromJson(modules.get(i).getAsJsonObject());
                JsonObject newStudyModuleJson = connectAPI(newStudyModuleAPI, "groupId");
                //System.out.println(newStudyModuleJson);
                StudyModule newStudyModule = JsonToStudyModule(newStudyModuleJson);
                studyModule.addSubModule(newStudyModule);
                studyModuleRecursive(newStudyModuleJson, newStudyModule);
            }
        }
    }
    
    // call API containing all degree and return JsonArray contains all degrees.
    public JsonArray callAllDegrees(){
        return connectAPI(degreeListAPI, "id").get("searchResults").getAsJsonArray();
    }
    
    public String getDegreeDetailAPI() {
        return degreeDetailAPI;
    }
    public ArrayList<DegreeProgram> getDegrees() {
        return degrees;
    }
    public String getStudyModuleAPI() {
        return studyModuleAPI;
    }
    public String getIdentifierTUNI() {
        return identifierTUNI;
    }
    // Example of a main function call
    public void mainLookLike(){
        //----------call API degree list -------------------------------------
        JsonArray degreeArray = callAllDegrees();
        // Take the first degree from the list
        JsonObject degreeOverview = degreeArray.get(0).getAsJsonObject();
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
        for (int i=0; i<degreeRules.size(); i++){
            degree.addStudyModule(degreeRules.get(i).getAsJsonObject());
        }

        // ---------------- take submodules/courses from degree detail----------------

        // call out 1 module (usually suppose to be only 1) ----------------------------
        JsonObject studyModuleOverview = degreeRules.get(0).getAsJsonObject();
        String currentStudyModuleAPI = studyModuleAPI + studyModuleOverview.get("moduleGroupId").getAsString() + identifierTUNI;
        // contain: submodule or course
        JsonObject studyModuleDetail = connectAPI(currentStudyModuleAPI, "groupId");
        StudyModule firStudyModule = JsonToStudyModule(studyModuleDetail);
        // create that StudyModule structure
        studyModuleRecursive(studyModuleDetail, firStudyModule);
    }

}
