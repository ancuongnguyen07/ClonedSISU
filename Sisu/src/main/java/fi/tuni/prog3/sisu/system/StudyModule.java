/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

/**
 *
 * @author Cuong Nguyen && An Nguyen 
 */
public class StudyModule extends AbstractModule{
  // Link: https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=uta-ok-ykoodi-41176&universityId=tuni-university-root-id 
  private SubCompositeRule compositeRule;

  public StudyModule(String name, String id, String groupID, String API) {
    super(name, id, groupID, API);
    
  }

  public void setCompositeRule(SubCompositeRule compositeRule){
    this.compositeRule = compositeRule;
  }

  public SubCompositeRule getCompositeRule(){
    return compositeRule;
  }
    
}
