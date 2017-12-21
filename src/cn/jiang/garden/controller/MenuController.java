package cn.jiang.garden.controller;

import java.io.IOException;
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

import cn.jiang.garden.model.TFileEntity;
import cn.jiang.garden.model.TMenuEntity;
import cn.jiang.garden.service.FileService;
import cn.jiang.garden.service.TMenuService;
import cn.jiang.garden.utils.DataWrapper;

/**
 * Created by tia on 16/5/30.
 */
@Controller
@RequestMapping(value="api/menu")
public class MenuController {
    @Autowired
    TMenuService tMenuService;
    @Autowired
    FileService fileService;

    //得到菜单列表 api/menu/getMenuList?token=x 已测
    /*
     * type	Integer	好像是1-7，代表周一到周日田程元做的，如果界面上找不到，问他
     * timeType	Integer	好像是 0-早餐，1-午餐，2-晚餐，田程元做的，如果界面上找不到，问他
     * 
     * */
    @RequestMapping(value="getMenuList",params = "token")
    @ResponseBody
    public DataWrapper<List<TMenuEntity>> getMenuList(
            @RequestParam(value = "type",required = false) Integer type,
            @RequestParam(value = "timeType",required = false) Integer timeType,
            @RequestParam(value = "token",required = false) String token){
        return tMenuService.getMenuList(type,timeType,token);
    }
    //删除菜单 api/menu/deleteMenuList?token =x&menuId=x 已测
    @RequestMapping(value="deleteMenu",params = "token")
    @ResponseBody
    public DataWrapper<Void> deleteMenuList(
            @RequestParam(value = "menuId",required = false) Long menuId,
            @RequestParam(value = "token",required = false) String token){
        return tMenuService.deleteMenu(menuId,token);
    }

    //增加菜单 api/menu/addMenuList?token = x 已测
    /*
     * name	String	菜单名称，好像是根据1~7，对应周一~周日，也就是说name=周一
     * type	Integer	好像是1-7，代表周一到周日田程元做的，如果界面上找不到，问他
     * imgId	Long	菜品图片文件的id，这个id可以从菜品列表中获取
     * timeType Integer 好像是 0-早餐，1-午餐，2-晚餐，田程元做的，如果界面上找不到，问他
     * 
     * */
    @RequestMapping(value="addMenu",params = "token")
    @ResponseBody
    public DataWrapper<Void> addMenu(
            @ModelAttribute TMenuEntity menuEntity,
            @RequestParam(value = "token",required = false) String token){
        return tMenuService.addMenu(menuEntity,token);
    }




    //添加菜品 api/menu/addMenuItem?token=x
    /*
     * file 文件流	菜品的图片
     * name	String	菜品名称
     * type	Integer	不用传，默认为7（1-6主页6张图， 7 菜品， 8 新闻， 9 简历， 10 照片，11空文件）
     * intro	String	菜品简介
     * imgSrc String 图片路径，后台生成
     * 
     * */
    @RequestMapping(value="addMenuItem",method= RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> addMenuItem(
            @ModelAttribute TFileEntity tFile,
            @RequestParam MultipartFile file,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
        tFile.setType(7);
        return fileService.uploadFile(request,token,tFile,file);

    }
    //更新菜品 api/menu/updateMenuItem?token=x
    /*
     * id Long 菜品id
     * file 文件流	菜品的图片
     * name	String	菜品名称
     * type	Integer	不用传，默认为7（1-6主页6张图， 7 菜品， 8 新闻， 9 简历， 10 照片，11空文件）
     * intro	String	菜品简介
     * imgSrc String 不用传，图片路径，后台生成
     * 
     * */
    @RequestMapping(value="updateMenuItem",method= RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> updateMenuItem(
            @ModelAttribute TFileEntity tFile,
            @RequestParam MultipartFile file,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
        tFile.setType(7);
        return fileService.updateFile(request,token,tFile,file);
    }
    //删除菜品 api/menu/deleteMenuItem/{itemId}?token=x
    @RequestMapping(value="deleteMenuItem/{itemId}",params = "token")
    @ResponseBody
    public DataWrapper<Void> deleteMenuItem(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "token",required = false) String token,
            HttpServletRequest request) throws IOException {
        return fileService.deleteFile(itemId,token,request);
    }
    //得到菜品列表 api/menu/getMenuItemList?token = x (type = 1~7:周一~周日 name:星期几 image:菜品的名字简介在json image类中) 已测，注：这是田程元之前写的注释
    @RequestMapping(value="getMenuItemList",params = "token")
    @ResponseBody
    public DataWrapper<List<TFileEntity>> getMenuItemList(
            @RequestParam(value = "token",required = false) String token){
        return tMenuService.getMenuItemList(token);
    }
}
