package com.odong.portal.controller.editor;

import com.odong.portal.ueditor.FileUploader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-13
 * Time: 上午9:20
 */
@Controller("c.file")
public class FileController {
    @RequestMapping(value = "/attachments/**", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource getAttach(HttpServletRequest request){
        return new FileSystemResource(fileUploader.getPhysicalPath(request.getRequestURI().substring("/attachments/".length())));
    }

    @RequestMapping(value = "/editor/fileUp", method = RequestMethod.POST)
    void postFileUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().print(fileUploader.fileUp(request));
    }


    @RequestMapping(value = "/editor/getContent", method = RequestMethod.POST)
    void postContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = request.getParameter("myEditor");
        response.getWriter().print("第1个编辑器的值");
        response.getWriter().print("<div class='content'>" + content + "</div>");
    }

    @RequestMapping(value = "/editor/getMovie", method = RequestMethod.POST)
    void postMovie(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().print(fileUploader.movie(request));
    }

    @RequestMapping(value = "/editor/getRemoteImage", method = RequestMethod.POST)
    void postRemoteImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().print(fileUploader.remoteImage(request));
    }


    @RequestMapping(value = "/editor/imageManager", method = RequestMethod.POST)
    void postImageManager(HttpSession session, HttpServletResponse response) throws IOException {
        response.getWriter().print(fileUploader.imageManager(session));
    }

    @RequestMapping(value = "/editor/imageUp", method = RequestMethod.POST)
    void postImageUp(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.getWriter().print(fileUploader.imageUp(request));
    }

    @RequestMapping(value = "/editor/scrawlUp", method = RequestMethod.POST)
    void postScrawlUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().print(fileUploader.scrawlUp(request));
    }

    @Resource
    private FileUploader fileUploader;


    public void setFileUploader(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }
}
