package com.file.manager.ui.ctrls;

import static com.file.manager.ui.util.Utils.defaultData;
import static com.file.manager.ui.util.Utils.getFilesAndFolders;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.file.manager.ui.entities.SearchCriteria;

import file.operations.FileUtil;

/**
 *
 * @author anupkalshetty
 */

@org.springframework.stereotype.Controller
public class Controller {

	@RequestMapping("/backup")
	public String backup(ModelMap m, @ModelAttribute("searchCriteria") SearchCriteria sc,
			@RequestParam(name = "selectedValues") List<String> selectedValues) {
		FileUtil.backupFiles(sc.getWorkingDir(), sc.getBackupPath(),
				selectedValues.parallelStream().map(s -> new File(s)).collect(Collectors.toList()));
		m.put("searchCriteria", sc);
		return "redirect:/";
	}

	@RequestMapping("/delete")
	public String delete(ModelMap m, @ModelAttribute("searchCriteria") SearchCriteria sc,
			@RequestParam(name = "selectedValues") List<String> selectedValues) {
		FileUtil.delete(selectedValues.parallelStream().map(s -> new File(s)).collect(Collectors.toList()));
		m.put("searchCriteria", sc);
		return "redirect:/";
	}

	@RequestMapping("/editFile")
	public String editFile(ModelMap m, @RequestParam(name = "value") String value) {
		m.put("fileName", value);
		m.put("fileContent", FileUtil.readFile(value));
		return "editFile";
	}

	@RequestMapping("/save")
	public String save(ModelMap m, @RequestParam(name = "fileName") String fileName,
			@RequestParam(name = "fileContent") String fileContent, RedirectAttributes ra) {
		File f = new File(fileName);
		FileUtil.write(f.getName(), f.getParent(), fileContent);
		m.put("message", "saved successfully");
		ra.addFlashAttribute("value", fileName);
		ra.addAttribute("value", fileName);
		return "redirect:/editFile";
	}

	@RequestMapping("/restore")
	public String restore(ModelMap m, @ModelAttribute("searchCriteria") SearchCriteria sc) {
		FileUtil.restoreFiles(sc.getBackupPath());
		m.put("searchCriteria", sc);
		return "redirect:/";
	}

	@RequestMapping("/search")
	public String search(@ModelAttribute("searchCriteria") SearchCriteria sc, RedirectAttributes ra) {
		sc.setFoundRecords(getFilesAndFolders(sc));
		ra.addFlashAttribute("searchCriteria", sc);
		return "redirect:/";
	}

	@RequestMapping("/reset")
	public String reset(@ModelAttribute("searchCriteria") SearchCriteria sc, RedirectAttributes ra) {
		ra.addFlashAttribute("searchCriteria", defaultData());
		return "redirect:/";
	}

	@RequestMapping("/")
	public ModelAndView home(ModelMap m, @ModelAttribute("searchCriteria") SearchCriteria sc) {
		m.put("searchCriteria", (sc.getSearchTypes() == null) ? defaultData() : sc);
		m.put("searchTypes", SearchType.valueEnumMap().keySet());
		ModelAndView mv = new ModelAndView("index", m);
		return mv;
	}

	/*
	 * 
	 * 
	 * 
	 * @RequestMapping("/delete") public String delete(@RequestParam("ids")
	 * List<Long> ids){ if(!CollectionUtils.isEmpty(ids)) {
	 * //genericService.deleteById(ids); } return "redirect:/"; }
	 * 
	 * 
	 * @RequestMapping("/deleteField") public String
	 * deleteField(@ModelAttribute("employee") Employee e,@RequestParam("ids")
	 * List<Integer> indexes,@RequestParam("field") String field,RedirectAttributes
	 * ra){ if(!CollectionUtils.isEmpty(indexes) && field!=null) {
	 * if(field.equals("companies")) { List<Company> companies = e.getCompanies();
	 * Utils.removeElements(companies, indexes); e.setCompanies(companies); } }
	 * ra.addFlashAttribute("employee", e); return "redirect:/"; }
	 * 
	 * @RequestMapping("/loadDefaultData") public String
	 * loadDefaultData(RedirectAttributes ra){ Employee e = Utils.getDefaultData();
	 * //genericService.save(e); ra.addFlashAttribute("employee", e); return
	 * "redirect:/"; }
	 * 
	 * 
	 * @RequestMapping("/modifySkills") public String
	 * modifySkills(@ModelAttribute("employee") Employee
	 * e,@RequestParam(name="indices",required=false) List<Integer>
	 * indices,@RequestParam("action") String action,RedirectAttributes ra){
	 * if(action!=null) { List<Skill> skills = e.getSkills(); if(skills==null)
	 * skills=new ArrayList<Skill>(); Skill s=new Skill();
	 * if(!CollectionUtils.isEmpty(indices) && action.equals("delete"))
	 * Utils.removeElements(skills, indices); if(action.equals("add"))
	 * skills.add(s); e.setSkills(skills); } ra.addFlashAttribute("employee", e);
	 * return "redirect:/"; }
	 * 
	 * @RequestMapping("/modifyProject") public String
	 * modifyProject(@ModelAttribute("employee") Employee
	 * e,@RequestParam("companyIndex") Integer
	 * companyIndex,@RequestParam("projectIndex") Integer
	 * projectIndex,@RequestParam("action") String action,RedirectAttributes ra){
	 * if(companyIndex!=null && projectIndex!=null && action!=null) { List<Project>
	 * projects=e.getCompanies().get(companyIndex).getProjects(); Project p =
	 * projects.get(projectIndex); if(action.equals("delete")) { projects.remove(p);
	 * } if(action.equals("duplicate")) { Project p1=(Project) p.clone();
	 * projects.add(p1); } e.getCompanies().get(companyIndex).setProjects(projects);
	 * } ra.addFlashAttribute("employee", e); return "redirect:/"; }
	 * 
	 * @RequestMapping("/duplicateField") public String
	 * duplicateField(@ModelAttribute("employee") Employee e,@RequestParam("ids")
	 * List<Byte> ids,@RequestParam("field") String field,RedirectAttributes ra){
	 * if(!CollectionUtils.isEmpty(ids) && field!=null) {
	 * if(field.equals("companies")) { List<Company> companies = e.getCompanies();
	 * List<Company> companiesToBeDuplicated = new ArrayList<Company>(); for(byte
	 * i=0;i<companies.size();i++) if(ids.contains(i))
	 * companiesToBeDuplicated.add(companies.get(i)); List<Company> copiedCompanies
	 * = (List<Company>) Utils.COPY_LIST.apply(companiesToBeDuplicated);
	 * companies.addAll(copiedCompanies); e.setCompanies(companies); } }
	 * ra.addFlashAttribute("employee", e); return "redirect:/"; }
	 * 
	 * @RequestMapping("/duplicate") public String duplicate(@RequestParam("ids")
	 * List<Long> ids){ if(!CollectionUtils.isEmpty(ids)) { for(Long id:ids) {
	 * Employee e = new Employee(); Employee e2 = (Employee) e.clone();
	 * //genericService.save(e2); } } return "redirect:/"; }
	 * 
	 * @RequestMapping("/addMore") public String addMore(@ModelAttribute("employee")
	 * Employee e,@RequestParam(name="field",required=true) String
	 * field,@RequestParam(name="companyIndex",required=false)Integer
	 * companyIndex,RedirectAttributes ra){ List<Company> companies =
	 * e.getCompanies(); Company c = new Company(); if(companies==null) companies =
	 * new ArrayList<Company>(); Project p = new Project();
	 * if(field.equals("companies")) { companies.add(c); e.setCompanies(companies);
	 * } if(field.equals("projects")) { for(int ci=0;ci<companies.size();ci++)
	 * if(ci==companyIndex) { Company cp = companies.get(ci); List<Project> projects
	 * = cp.getProjects(); if(projects==null) projects=new ArrayList<Project>();
	 * projects.add(p); cp.setProjects(projects); e.setCompanies(companies); } }
	 * ra.addFlashAttribute("employee", e); return "redirect:/"; }
	 * 
	 * @RequestMapping("/edit/{eid}") public String edit(@PathVariable Long
	 * eid,RedirectAttributes ra){ Employee e = (eid==null)?new Employee():new
	 * Employee(); ra.addFlashAttribute("employee", e); return "redirect:/"; }
	 * 
	 * @RequestMapping("/otherDetails/{eid}") public ModelAndView
	 * otherDetails(@PathVariable Long eid, ModelMap m){ Employee e =
	 * (eid==null)?new Employee():new Employee(); m.put("o", new OtherDetails(e));
	 * return new ModelAndView("otherDetails",m); }
	 * 
	 * 
	 * @RequestMapping("/save") public ModelAndView
	 * save(@ModelAttribute("employee")Employee e, ModelMap m){
	 * System.out.print("\nsave : \nname : "+e+"\n"); //genericService.save(e);
	 * return new ModelAndView("redirect:/",m); }
	 * 
	 * @RequestMapping("/showSavedDetails") public ModelAndView
	 * showSavedDetails(@ModelAttribute("employee")Employee
	 * e, @RequestParam(name="deletedIds",required=false) List<Long> deletedIds,
	 * ModelMap m){ List<Employee> employees = new ArrayList<Employee>();
	 * m.put("employees", employees); return new ModelAndView("savedDetails",m); }
	 * 
	 */

	static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}

}
