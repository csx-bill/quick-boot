package com.quick.boot.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.boot.modules.common.vo.R;
import com.quick.boot.modules.system.entity.SysFiles;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SysFilesService extends IService<SysFiles> {

    /**
     * 上传文件
     * @param file 文件流
     * @param type 类型
     * @return
     */
    R uploadFile(MultipartFile file, String type);

    /**
     * 读取文件
     * @param fileName 文件名称
     * @param response 输出流
     */
    void getFile(String fileName, HttpServletResponse response);

}
