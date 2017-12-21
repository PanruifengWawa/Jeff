package cn.jiang.garden.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.jiang.garden.enums.EducationEnum;
import cn.jiang.garden.model.TJobApplicationEntity;
import cn.jiang.garden.service.FileService;
import cn.jiang.garden.service.JobApplicationService;
import cn.jiang.garden.utils.DataWrapper;

@Controller
@RequestMapping(value="api/jobApplication")
public class JobApplicationController {
    @Autowired
    FileService fileService;

    @Autowired
    JobApplicationService jobApplicationService;

    //投递简历  api/jobApplication/addJobApplication    POST
    /*
    name      String
    sex       Integer   1 man   0 male
    school    学校 String
    experience double 工作经验
    education String
    address   String
    tel       String
    mail      String
    birthDay  Date
    photo     图片文件
    resume    简历文件
     */
    //注意，其中的education存的是字符串如“初中”，有哪一些请看enums包下的EducationEnum，如需修改具体内容，请一起修改EducationEnum
    @RequestMapping(value="addJobApplication",method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> addJobApplication(
            @ModelAttribute TJobApplicationEntity jobApplicationEntity,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            HttpServletRequest request
    ){
        return  jobApplicationService.addJobApplication( jobApplicationEntity,photo,resume,request);
    }

    //管理员查看简历  api/jobApplication/getJobApplicationList?token=    GET
    
    /*
     * fromAge int 	年龄范围
     * toAge	int 年龄范围
     * educationType	Integer  学历，学历的type对应具体内容，如1=初中，2=高中/高职，3=本科，4=硕士，5=博士
     * fromExperience	Integer	工作年数范围
     * toExperience		Integer	工作年数范围
     * fromDate			String	递交日期范围
     * toDate			String	递交日期范围
     */
    @RequestMapping(value="getJobApplicationList",method = RequestMethod.GET)
    @ResponseBody
    public DataWrapper<List<TJobApplicationEntity>> getJobApplicationList(
            @RequestParam(value = "fromAge",required = false) Integer fromAge,//年龄范围
            @RequestParam(value = "toAge",required = false) Integer toAge,
            @RequestParam(value = "educationType",required = false) Integer educationType,//学历，学历的type对应具体内容，如1=初中，具体请看enums包下的EducationEnum
            @RequestParam(value = "fromExperience",required = false) Double fromExperience,//工作经验
            @RequestParam(value = "toExperience",required = false) Double toExperience,
            @RequestParam(value = "fromDate",required = false) String fromDate,//递交日期
            @RequestParam(value = "toDate",required = false) String toDate,
            @RequestParam(value = "token",required = false) String token
    ){
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);//得到年
        String condition = "";
        if(fromAge != null) {
            condition += " jobApplication.birth <= '" + (currentYear - fromAge) + "-12-31'";
        }
        if(toAge != null) {
            if(condition != "") condition += " and";
            condition += " jobApplication.birth >= '" + (currentYear - toAge) + "-01-01'";
        }
        if(educationType!= null && EducationEnum.parse(educationType) != null) {
            if(condition != "") condition += " and";
            condition += " jobApplication.education = '" + EducationEnum.parse(educationType) + "'";
        }
        if(fromExperience != null) {
            if(condition != "") condition += " and";
            condition += " jobApplication.experience >= " + fromExperience;
        }
        if(toExperience != null) {
            if(condition != "") condition += " and";
            condition += " jobApplication.experience <= " + toExperience;
        }
        if(fromDate != null) {
            if(condition != "") condition += " and";
            condition += " jobApplication.applicated_date >= '" + fromDate + "'";
        }
        if(toDate != null) {
            if(condition != "") condition += " and";
            condition += " jobApplication.applicated_date <= '" + toDate + "'";
        }
        System.out.println(condition);
        return  jobApplicationService.getJobApplicationList(token,condition);
    }

    //管理员删除简历  api/jobApplication/deleteJobApplication/{jobApplicationId}?token=    DELETE
    @RequestMapping(value="deleteJobApplication/{jobApplicationId}",method = RequestMethod.DELETE)
    @ResponseBody
    public DataWrapper<Void> deleteJobApplicationList(
            @PathVariable("jobApplicationId") Long jobApplicationId,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request
    ){
        return  jobApplicationService.deleteJobApplication(token,jobApplicationId,request);
    }

}
