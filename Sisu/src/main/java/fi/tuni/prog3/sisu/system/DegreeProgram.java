/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *  The structure represent a degree
 * @author Cuong Nguyen && An Nguyen
 */
public class DegreeProgram extends AbstractModule{
  // Link all degree: https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000
  // Link specific degree: https://sis-tuni.funidata.fi/kori/api/modules/otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b

  private SubCompositeRule compositeRule;

  /**
   * Constructor for degree program, set needed information for it.
   * @param name
   * @param id
   * @param groupID
   * @param API
   * @param compositeRule
   */
  public DegreeProgram(String name, String id, String groupID, String API, SubCompositeRule compositeRule) {
    super(name, id, groupID, API);
    this.compositeRule = compositeRule;

  }

  /**
   * Return the CompositeRule that this degree has.
   * @return the CompositeRule that this degree has.
   */
  public SubCompositeRule getCompositeRule(){
    return compositeRule;
  }

 
}
