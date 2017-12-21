package cn.jiang.garden.controller;

import cn.jiang.garden.model.TFileEntity;
import cn.jiang.garden.model.TNewsEntity;
import cn.jiang.garden.service.FileService;
import cn.jiang.garden.service.TNewsService;
import cn.jiang.garden.utils.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by tia on 16/5/30.
 */
@Controller
@RequestMapping(value="api/news")
public class NewsController {
    @Autowired
    TNewsService tNewsService;
    @Autowired
    FileService fileService;
    //添加新闻 api/news/addNews?token=x  POST
    /*
     * files	MultipartFile[]文件数组 	新闻图片列表
     * name 	 String 		  新闻名称
     * intro 	String 			  新闻介绍
     * publishDate		 Date 		  发布日期
     * type 			Integer 			  0主页 1新闻，这里是2
     * Long 			imgId1	   新闻图1的id（横幅最多有7张图）下面的一样，这些id不需要设置，在文件保存后，每个文件会自动生成
     * Long imgId2
     * Long imgId3
     * Long imgId4
     * Long imgId5
     * Long imgId6
     * Long imgId7
     * token    用户凭证 String
     * */
    @RequestMapping(value="addNews",method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> addNews(
            @ModelAttribute TNewsEntity tNewsEntity,
            @RequestParam(value = "files",required = false) MultipartFile[] files,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
        for(int i=0;i<files.length;i++){
            TFileEntity file = new TFileEntity();
            file.setType(8);
            fileService.uploadFile(request,token,file,files[i]);
            if(i == 0){
                tNewsEntity.setImgId1(file.getId());
            }else if(i == 1){
                tNewsEntity.setImgId2(file.getId());
            }else if(i == 2){
                tNewsEntity.setImgId3(file.getId());
            }else if(i == 3){
                tNewsEntity.setImgId4(file.getId());
            }else if(i == 4){
                tNewsEntity.setImgId5(file.getId());
            }else if(i == 5){
                tNewsEntity.setImgId6(file.getId());
            }else if(i == 6){
                tNewsEntity.setImgId7(file.getId());
            }
        }
        return tNewsService.addNews(tNewsEntity,token);
    }
    
    
    //更新新闻 api/news/updateNews?token=x
    /*
     * files	MultipartFile[]文件数组 	新闻图片列表
     * id	Long		新闻id
     * name 	 String 		  新闻名称
     * intro 	String 			  新闻介绍
     * publishDate		 Date 		  发布日期
     * type 			Integer 			  0主页 1新闻，这里是2
     * Long 			imgId1	   新闻图1的id（横幅最多有7张图）下面的一样，这些id不需要设置，在文件保存后，每个文件会自动生成，注有几张图，那么则有对赢imgId
     * Long imgId2
     * Long imgId3
     * Long imgId4
     * Long imgId5
     * Long imgId6
     * Long imgId7
     * token    用户凭证 String
     * */
    @RequestMapping(value="updateNews",method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> updateNews(
            @ModelAttribute TNewsEntity tNewsEntity,
            @RequestParam MultipartFile[] files,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
            for(int i=0;i<files.length;i++){
                TFileEntity file = new TFileEntity();
                file.setType(8);
                fileService.uploadFile(request,token,file,files[i]);
                if(i == 0){
                    tNewsEntity.setImgId1(file.getId());
                }else if(i == 1){
                    tNewsEntity.setImgId2(file.getId());
                }else if(i == 2){
                    tNewsEntity.setImgId3(file.getId());
                }else if(i == 3){
                    tNewsEntity.setImgId4(file.getId());
                }else if(i == 4){
                    tNewsEntity.setImgId5(file.getId());
                }else if(i == 5){
                    tNewsEntity.setImgId6(file.getId());
                }else if(i == 6){
                    tNewsEntity.setImgId7(file.getId());
                }
            }
        return tNewsService.updateNews(tNewsEntity,token);
    }
    //删除新闻 api/news/deleteNews/{newsId}?token=x
    @RequestMapping(value="deleteNews/{newsId}",params = "token")
    @ResponseBody
    public DataWrapper<Void> deleteNews(
            @PathVariable("newsId") Long tnewsId,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
        TNewsEntity tNewsEntity = tNewsService.getNewsById(tnewsId,token).getData();
        DataWrapper<Void> result = tNewsService.deleteNews(tnewsId,token);
        fileService.deleteFile(tNewsEntity.getImgId1(),token,request);
        fileService.deleteFile(tNewsEntity.getImgId2(),token,request);
        fileService.deleteFile(tNewsEntity.getImgId3(),token,request);
        fileService.deleteFile(tNewsEntity.getImgId4(),token,request);
        fileService.deleteFile(tNewsEntity.getImgId5(),token,request);
        fileService.deleteFile(tNewsEntity.getImgId6(),token,request);
        fileService.deleteFile(tNewsEntity.getImgId7(),token,request);
        return result;
    }
    //新闻列表 api/news/getNewsList?token=x  已测
    @RequestMapping(value="getNewsList",params = "token")
    @ResponseBody
    public DataWrapper<List<TNewsEntity>> getNewsList(
            @RequestParam(value = "token",required = false) String token
    ){
        return tNewsService.getNewsList(token);
    }
    //得到单个新闻信息 api/news/getNewsById/{newsId}?token=x  已测
    @RequestMapping(value="getNewsById/{newsId}",params = "token")
    @ResponseBody
    public DataWrapper<TNewsEntity> getNewsById(
            @PathVariable("newsId") Long newsId,
            @RequestParam(value = "token",required = false) String token
    ){
        return tNewsService.getNewsById(newsId,token);
    }
}
