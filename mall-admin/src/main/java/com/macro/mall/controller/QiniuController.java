package com.macro.mall.controller;


import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.util.IdUtils;
import com.macro.mall.common.util.QiniuUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Qiniu相关操作接口
 * Created by macro on 2018/4/26.
 */
@RestController
@Api(tags = "QiniuController", description = "Qiniu管理")
@RequestMapping("/qiniuyun")
public class QiniuController {
    @Resource
    private QiniuUtil qiniuUtil;

    /**
     * 上传文件到七牛云存储
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/img/upload")
    public CommonResult uploadImgQiniu(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
        String url = qiniuUtil.uploadImg(inputStream, String.valueOf(IdUtils.snowflakeId()));
        return CommonResult.success(url,"上传成功");
    }

}
