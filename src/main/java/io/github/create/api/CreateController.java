package io.github.create.api;

import io.github.create.api.factory.CreateApiFactory;
import io.github.create.api.factory.CreateHtmlByVm;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/io/github/create/api")
public class CreateController {
    @RequestMapping()
    public void getApiDocZip(HttpServletResponse response)throws Exception{
        byte[] data = CreateHtmlByVm.create(new CreateApiFactory().createApi());
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"apiDoc.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    //Java-CP ./tools.jar test
}
