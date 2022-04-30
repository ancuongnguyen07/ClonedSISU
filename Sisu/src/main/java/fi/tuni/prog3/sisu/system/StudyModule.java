/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

/**
 *  The structure represent a StudyModule
 * @author Cuong Nguyen && An Nguyen
 */
public class StudyModule extends AbstractModule{
  // Link: https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=uta-ok-ykoodi-41176&universityId=tuni-university-root-id 
  private SubCompositeRule compositeRule;

  /**
   * Constructor for StudyModule, set needed information for it.
   * @param name
   * @param id
   * @param groupID
   * @param API
   */
  public StudyModule(String name, String id, String groupID, String API) {
    super(name, id, groupID, API);
    
  }

  /**
   * Set the compositeRule for the degree because it's recursive so not in the Constructor
   * @param compositeRule
   */
  public void setCompositeRule(SubCompositeRule compositeRule){
    this.compositeRule = compositeRule;
  }

  /**
   * Return the CompositeRule that this StudyModule has.
   * @return the CompositeRule that this StudyModule has.
   */
  public SubCompositeRule getCompositeRule(){
    return compositeRule;
  }
    
}
