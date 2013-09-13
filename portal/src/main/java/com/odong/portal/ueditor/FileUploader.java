package com.odong.portal.ueditor;

import com.odong.portal.model.SessionItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-13
 * Time: 上午9:23
 */
@Component("file.uploader")
public class FileUploader {

    public String imageUp(HttpServletRequest request) {
        Attachment attach = upload(request, imageTypes);
        return "{'original':'" + attach.getOriginalName() + "','url':'" + attach.getUrl() + "','title':'" + attach.getTitle() + "','state':'" + attach.getState() + "'}";
    }

    public String scrawlUp(HttpServletRequest request) {

        Attachment attach;
        switch (request.getParameter("action")) {
            case "tmpImg":
                attach = upload(request, imageTypes);
                return "<script>parent.ue_callback('" + attach.getUrl() + "','" + attach.getState() + "')</script>";

            default:
                attach = uploadBase64(request, "content", imageTypes);
                return "{'url':'" + attach.getUrl() + "',state:'" + attach.getState() + "'}";
        }
    }

    public String imageManager(HttpSession session) {
        String path = getPhysicalPath(getSelfPath(session));
        String url = getServletUrl(getSelfPath(session));
        List<File> files = listImages(path, new ArrayList<>());
        StringBuilder sb = new StringBuilder();
        files.forEach((f) -> {
            sb.append(f.getPath().replace(path, url));
            sb.append("ue_separate_ue");
        });
        return sb.toString();
    }

    public String remoteImage(HttpServletRequest request) throws IOException {

        String srcUrl = request.getParameter("upfile");
        String tip = "";
        String url = "";
        for (String imgUrl : srcUrl.split("ue_separate_ue")) {
            Attachment a = getRemoteImage(imgUrl, request.getSession());
            url += a.getFileName() + "ue_separate_ue";
            tip += a.getState() + "\n";
        }

        url = url.substring(0, url.lastIndexOf("ue_separate_ue"));
        return "{'url':'" + url + "','tip':'" + tip + "','srcUrl':'" + srcUrl + "'}";
    }

    public String movie(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String searchKey = request.getParameter("searchKey");
        String videoType = request.getParameter("videoType");
        try {
            searchKey = URLEncoder.encode(searchKey, "utf-8");
            URL url = new URL("http://api.tudou.com/v3/gw?method=item.search&appKey=myKey&format=json&kw=" + searchKey + "&pageNo=1&pageSize=20&channelId=" + videoType + "&inDays=7&media=v&sort=s");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
        } catch (MalformedURLException e) {
            logger.error("URL错误", e);
        } catch (IOException e) {
            logger.error("IO异常", e);
        }
        return sb.toString();
    }

    public String fileUp(HttpServletRequest request) {
        Attachment att = upload(request, upTypes);
        return "{'url':'" + att.getUrl() + "','fileType':'" + att.getType() + "','state':'" + att.getState() + "','original':'" + att.getOriginalName() + "'}";
    }

    private Attachment upload(HttpServletRequest request, String[] fileTypes) {
        Attachment attach = new Attachment();
        attach.setState(Attachment.State.SUCCESS);

        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory dff = new DiskFileItemFactory();
            String servletPath = getSelfPath(request.getSession())+"/" +getDatePath();
            String savePath = getPhysicalPath(servletPath);
            dff.setRepository(new File(savePath));
            try {
                ServletFileUpload sfu = new ServletFileUpload(dff);
                sfu.setSizeMax(maxSize * 1024);
                sfu.setHeaderEncoding("utf-8");
                FileItemIterator fii = sfu.getItemIterator(request);
                while (fii.hasNext()) {
                    FileItemStream fis = fii.next();
                    if (!fis.isFormField()) {
                        attach.setOriginalName(fis.getName().substring(fis.getName().lastIndexOf(System.getProperty("file.separator")) + 1));
                        if (!this.checkFileType(attach.getOriginalName(), fileTypes)) {
                            attach.setState(Attachment.State.TYPE);
                            continue;
                        }
                        attach.setFileName(getFileName(attach.getOriginalName()));
                        attach.setType(getFileExt(attach.getFileName()));
                        attach.setUrl(servletPath+"/"+attach.getFileName());
                        BufferedInputStream in = new BufferedInputStream(fis.openStream());
                        FileOutputStream out = new FileOutputStream(new File(savePath+"/"+attach.getFileName()));
                        BufferedOutputStream output = new BufferedOutputStream(out);
                        Streams.copy(in, output, true);
                        //UE中只会处理单张上传，完成后即退出
                        break;
                    } else {
                        String fname = fis.getFieldName();
                        //只处理title，其余表单请自行处理
                        if(!fname.equals("pictitle")){
                            continue;
                        }
                        BufferedInputStream in = new BufferedInputStream(fis.openStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder result = new StringBuilder();
                        while (reader.ready()) {
                            result.append((char)reader.read());
                        }
                        attach.setTitle(result.toString());
                        reader.close();
                    }
                }
            } catch (FileUploadBase.SizeLimitExceededException e) {
                logger.error("上传文件大小超限", e);
                attach.setState(Attachment.State.SIZE);
            } catch (FileUploadBase.InvalidContentTypeException e) {
                logger.error("错误上传文件类型", e);
                attach.setState(Attachment.State.ENTYPE);
            } catch (FileUploadException e) {
                logger.error("错误上传文件请求", e);
                attach.setState(Attachment.State.REQUEST);
            } catch (Exception e) {
                logger.error("上传文件未知错误", e);
                attach.setState(Attachment.State.UNKNOWN);
            }
        }
        else {
            attach.setState(Attachment.State.NOFILE);
        }
       return attach;
    }

    private Attachment uploadBase64(HttpServletRequest request, String fieldName, String[] fileTypes) {


        //FIXME 文件后缀名
        String fileName = getSelfPath(request.getSession())+"/"+getDatePath()+"/"+getFileName("");
        String path = getPhysicalPath(fileName);
        String url = getServletUrl(fileName);
        Attachment.State   state = Attachment.State.SUCCESS;
        String base64Data = request.getParameter(fieldName);

        try(OutputStream ro = new FileOutputStream(new File(path))) {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(base64Data);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            ro.write(b);
            ro.flush();
        } catch (IOException e) {
            state = Attachment.State.IO;
            logger.error("IO异常", e);
        }
        Attachment attach = new Attachment();
        attach.setUrl(url);
        attach.setState(state);
        return attach;
    }

    private boolean checkFileType(String fileName, String[] fileTypes) {
        for (String ext : fileTypes) {
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }


    private List<File> listImages(String path, List<File> files) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list != null) {
                for (File f : list) {
                    if (f.isDirectory()) {
                        listImages(f.getAbsolutePath(), files);
                    } else {
                        if (checkFileType(f.getName(), imageTypes)) {
                            files.add(f);
                        }
                    }
                }
            }

        }
        return files;
    }

    private Attachment getRemoteImage(String imgUrl, HttpSession session) throws IOException {
        Attachment attach = new Attachment();
        attach.setState(Attachment.State.SUCCESS);

        if (checkFileType(imgUrl, imageTypes)) {
            //大小验证
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection conn = (HttpURLConnection) new URL(imgUrl).openConnection();
            if (conn.getContentType().contains("image")) {
                if (conn.getResponseCode() == 200) {
                    attach.setState(Attachment.State.URL);
                } else {
                    String fileName = getSelfPath(session) + "/" + getDatePath() + "/" + getFileName(getFileExt(imgUrl));
                    File file = new File(getPhysicalPath(fileName));
                    attach.setFileName(getServletUrl(fileName));
                    try (InputStream is = conn.getInputStream();
                         OutputStream os = new FileOutputStream(file)) {
                        int b;
                        while ((b = is.read()) != -1) {
                            os.write(b);
                        }
                    } catch (IOException e) {
                        logger.error("页面无法访问或写入文件出错", e);
                        attach.setState(Attachment.State.IO);
                    }
                }
            } else {
                attach.setState(Attachment.State.HEADER);
            }

        } else {
            attach.setState(Attachment.State.TYPE);
        }
        return attach;
    }


    private String getServletUrl(String url) {
        return "/statics/" + url;
    }

    private String getPhysicalPath(String url) {
        return appStore + "/attach/" + url;
    }

    private String getSelfPath(HttpSession session) {
        SessionItem si = (SessionItem) session.getAttribute(SessionItem.KEY);
        return si == null ? "guest" : "u" + si.getSsUserId();
    }

    private String getDatePath() {
        String path = FORMAT.format(new Date());
        File dir = new File(getPhysicalPath(path));
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                logger.error("创建目录{}失败", path);
            }
        }
        return path;
    }

    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getFileName(String ext) {
        return UUID.randomUUID().toString() + ext;
    }


    @PostConstruct
    void init() {
        imageTypes = new String[]{".gif", ".png", ".jpg", ".jpeg", ".bmp"};
        upTypes = new String[]{".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv"};
    }

    private String[] imageTypes;
    private String[] upTypes;
    @Value("${app.store}")
    private String appStore;
    @Value("${upload.size}")
    private int maxSize;
    private final static DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static Logger logger = LoggerFactory.getLogger(FileUploader.class);

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setAppStore(String appStore) {
        this.appStore = appStore;
    }
}
