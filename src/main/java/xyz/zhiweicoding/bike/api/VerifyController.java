package xyz.zhiweicoding.bike.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 验证用
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/verify")
@Slf4j
public class VerifyController {

    @RequestMapping(value = "/{path}/{fileName}", method = {RequestMethod.POST, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.GET})
    public ResponseEntity<InputStreamResource> path(@PathVariable String path, @PathVariable String fileName) throws IOException {
        FileSystemResource file = new FileSystemResource("/" + path + "/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

}