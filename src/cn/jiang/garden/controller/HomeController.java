package cn.jiang.garden.controller;

import cn.jiang.garden.enums.ErrorCodeEnum;
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

/**
 * Created by tia on 16/5/31.
 */
@Controller
@RequestMapping(value="api/home")
public class HomeController {
    @Autowired
    TNewsService tNewsService;
    @Autowired
    FileService fileService;
    //得到主页数据 api/home/getHomeData?token=x     [GET]
    @RequestMapping(value="getHomeData")
    @ResponseBody
    public DataWrapper<TNewsEntity> getHomeData(
            @RequestParam(value = "token",required = false) String token){
        return tNewsService.getHomeData(token);
    }
    //修改横幅 api/home/updateHomeHeader     [POST]
    /*
    files    MultipartFile[]  文件流数组
    id 		 Long 			  横幅id
    name 	 String 		  横幅名称
    intro 	String 			  横幅介绍
    publishDate		 Date 		  发布日期
    type 			Integer 			  0主页 1新闻，这里是0
    Long 			imgId1			横幅图1的id（横幅有3张图），这些id不需要设置，在文件保存后，每个文件会自动生成，注：横幅的imgId4~imgId7弃用
    Long imgId2
    Long imgId3
    Long imgId4
    Long imgId5
    Long imgId6
    Long imgId7
    token    用户凭证 String

     */
    @RequestMapping(value="updateHomeHeader",method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> updateHomeHeader(
            @RequestParam MultipartFile[] files,
            @ModelAttribute TNewsEntity tNewsEntity,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
        if(files != null) {
            for (int i = 0; i < files.length; i++) {
                TFileEntity file = new TFileEntity();
                file.setType(8);
                fileService.uploadFile(request, token, file, files[i]);
                if (i == 0) {
                    tNewsEntity.setImgId1(file.getId());
                } else if (i == 1) {
                    tNewsEntity.setImgId2(file.getId());
                } else {
                    tNewsEntity.setImgId3(file.getId());
                }
            }
        }
        return tNewsService.updateNews(tNewsEntity,token);
    }
    //修改单个图片信息 api/home/updateHomePic   对应主页的1~6张图片 旁边的描述为tFileEntity 中的name 和 intro
    /*
     *  file    MultipartFile  文件
     *  name String 文件名称
     *  type  int	文件类型，1-6主页6张图， 7 菜品， 8 新闻， 9 简历， 10 照片，11空文件 （这里因为是主页的图片，所以type的范围是1~6）
     *  intro	String 介绍
     *  
     *  token    用户凭证 String
     * */
    @RequestMapping(value="updateHomePic",method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> updateHomePic(
            @ModelAttribute TFileEntity tFileEntity,
            @RequestParam MultipartFile file,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
        if(tFileEntity.getType() != null) {
            if (tFileEntity.getType() <= 6) {
                return fileService.updateFile(request, token, tFileEntity, file);
            }
        }
        DataWrapper<Void> ret = new DataWrapper<Void>();
        ret.setErrorCode(ErrorCodeEnum.Error);
        return ret;
    }
}
