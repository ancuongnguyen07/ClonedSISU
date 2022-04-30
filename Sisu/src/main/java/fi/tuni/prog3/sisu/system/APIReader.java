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
 *  A class to Read and Process information from API into the program structure. 
 * @author An Nguyen
 */
public class APIReader {
    private final String degreeListAPI = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";
    private final String degreeDetailAPI = "https://sis-tuni.funidata.fi/kori/api/modules/";
    private final String studyModuleAPI = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=";
    private final String courseUnitAPI = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=";
    private final String identifierTUNI = "&universityId=tuni-university-root-id";

    /**
     * Return the constant if the degree list provided in the project description.
     * @return The API containing basic information of all degrees (https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000)
     */
    public String getDegreeListAPI() {
        return degreeListAPI;
    }

    /**
     * Return the string constant of the degree specific API
     * @return The API format to add with degree ID to get information (https://sis-tuni.funidata.fi/kori/api/modules/)
     */
    public String getDegreeDetailAPI() {
        return degreeDetailAPI;
    }

    /**
     * Return the string constant of study module specific API
     * @return The API format to add with study module groupId (https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=) and with TUNI identifier to get information.
     */
    public String getStudyModuleAPI() {
        return studyModuleAPI;
    }

    /**
     * Return the string constant of course unit specific API
     * @return The API format to add with course unit groupId (https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=) and with TUNI identifier to get information.
     */
    public String getCourseUnitAPI() {
        return courseUnitAPI;
    }

    /**
     * Return the string constant of TUNI identifier on API
     * @return (&universityId=tuni-university-root-id)
     */
    public String getIdentifierTUNI() {
        return identifierTUNI;
    }
    
    
    /**
     * Connect to the given API and get information from it. Convert it to JsonObject and return it.
     * @param link The API to get information from.
     * @param type Because degree API is get by ID and other are get by groupId. The format is different so needed to handle seperately.
     * @return The JsonObject of the given API.
     */
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
    
    /**
     * Check null entity and the obj itself
     * @param obj the given JsonObject to check from
     * @param entity the checking entity
     * @return true if it's JsonNull or the object is Null, false otherwise.
     */
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

    /**
     * Check the content of given object. Will prioritize English over Finnish
     * @param content the content to check from
     * @return The content of the given object.
     */
    private String enOrFi(JsonObject content){
        if (checkNotJsonNull(content, "en")){
            return content.get("en").getAsString();
        } else {
            return content.get("fi").getAsString();
        }
    }

    /**
     * Create the appropriate API for StudyModule or CourseUnit in basic form (type of ModuleRule or CourseUnitRule) base on its type.
     * @param object The object to get API from
     * @return The API of the given object.
     * @throws AnException
     */
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

    /**
     * Create the subCompositeRule of the from the given Json rule
     * @param rule The rule need to be converted into SubCompositeRule class
     * @return The converted SubCompositeRule
     * @throws AnException When the rule have unexpected type (other than CompositeRule and CreditsRule)
     */
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
                if (subRules.size() == 1 && subRules.get(0).getAsJsonObject().get("type").getAsString().equals("CompositeRule")){
                    rule = subRules.get(0).getAsJsonObject();
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

    /**
     * Convert the given degree in Json to the class DegreeProgram and return it.
     * @param degreeJson The given Json object of the degree.
     * @return  The DegreeProgram class that is converted from degreeJson.
     * @throws AnException Throw when the rule type is not CompositeRule or CreditsRule.
     */
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
    
    /**
     * Convert the given study module in Json to the class StudyModule and return it.
     * @param JsonStudyModule The given Json object of the study module.
     * @return The StudyModule class converted from the JsonStudyModule.
     */
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
    
    /**
     * Convert the given course unit in Json to the class CourseUnit and return it.
     * @param courseUnitJson The given Json object of the course unit.
     * @return The course unit class converted from the courseUnitJson.
     */
    public CourseUnit JsonToCourseUnit(JsonObject courseUnitJson){
        String id = courseUnitJson.get("id").getAsString();
        // name have en or/and fi
        String name = null;
        if (checkNotJsonNull(courseUnitJson, "name")){
            name = enOrFi(courseUnitJson.get("name").getAsJsonObject());
        }
        String groupID = courseUnitJson.get("groupId").getAsString();
        int minCredit = -1;
        int maxCredit = -1;
        JsonObject credits = courseUnitJson.get("credits").getAsJsonObject();
        minCredit = credits.get("min").getAsInt();
        if (checkNotJsonNull(credits, "max")){
            maxCredit = credits.get("max").getAsInt();
        } else {
            maxCredit = minCredit;
        }
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
        return new CourseUnit(name, id, groupID, minCredit, maxCredit, API, content, 
                additional, learningMaterial, substitutions, completionMethods, courseCode, 
                gradeScaleId, outcomes, prerequisites, recommendedFormalPrerequisites,
                compulsoryFormalPrerequisites, studyFields, responsibilityInfos, 
                possibleAttainmentLanguages, curriculumPeriodIds, inclusionApplicationInstruction);
    }

    /**
     * Because the StudyModule is a deeply nested object, in which it can have subModules and subCourses. And it keep going deep down. So this function is to take the next level detail information of the given studyModule object. For example: studyModule with API (https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=otm-640dcf49-18b4-4392-8226-8cc18ea32dfb&universityId=tuni-university-root-id) will get the information of all the subModules that it's having. If not clicked yet, the created subModules is only in basic form (with name, id, groupId, API).
     * @param studyModule The studyModule to take next level
     * @throws AnException Throw when the studyModule rule type is not "CompositeRule" or "CreditsRule".
     */
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
    
    /**
     * Return the JsonArray of all degrees (get all degrees from the allDegreesAPI)
     * @return The array containing basic information of all degree in Json form.
     */
    public JsonArray callAllDegrees(){
        return connectAPI(degreeListAPI, "id").get("searchResults").getAsJsonArray();
    }
    
    /**
     * Return the list of subModules groupId.
     * @param studyModuleGroupId The studyModule that need to get subModules from.
     * @return The list of subModules groupId.
     */
    public ArrayList<String> getSubModulesGroupId(String studyModuleGroupId){
        String link = studyModuleAPI + studyModuleGroupId + identifierTUNI;
        JsonObject studyModuleJson = connectAPI(link, "groupId");
        StudyModule studyModule = JsonToStudyModule(studyModuleJson);
        ArrayList<StudyModule> subModules = studyModule.getCompositeRule().getSubModules();
        ArrayList<String> subModulesGroupId = new ArrayList<>();
        for (int i=0; i<subModules.size(); i++){
            subModulesGroupId.add( subModules.get(i).getGroupID());
        }
        return subModulesGroupId;
    }
}
