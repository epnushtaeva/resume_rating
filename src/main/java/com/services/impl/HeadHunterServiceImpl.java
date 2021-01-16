package com.services.impl;

import com.data_base.entities.Speciality;
import com.services.FileService;
import com.services.HeadHunterService;
import com.services.SpecialityService;
import org.apache.catalina.util.URLEncoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.glassfish.jersey.client.ClientConfig;

import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.nio.charset.StandardCharsets;


@Service
public class HeadHunterServiceImpl implements HeadHunterService {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private FileService fileService;

    @Transactional
    public void loadResumeFromHeadHunter(long specialityId, int pageFrom, int pagesCount){
        Speciality speciality = this.specialityService.getSpecialityById(specialityId);
        Client client = ClientBuilder.newClient( new ClientConfig().getConfiguration());

        String findSpecialitiesUrl = "https://ulyanovsk.hh.ru/search/resume?clusters=True&area=98&order_by=relevance&logic=normal&pos=full_text&exp_period=all_time&ored_clusters=True&st=resumeSearch&text=" + (new URLEncoder()).encode(speciality.getName(), StandardCharsets.UTF_8);

        for(int page = pageFrom; page < pageFrom + pagesCount; page++) {
            WebTarget webTarget = client.target(findSpecialitiesUrl);
            Response resp = webTarget.request("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("user-agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                    .get();
            String resumes = resp.readEntity(String.class);
            Document doc = Jsoup.parseBodyFragment(resumes);
            Elements resumesList = doc.getElementsByClass("resume-search-item__name");

            if(resumesList.size() == 0){
                break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(Element element: resumesList){
                 String resumeLink = "https://ulyanovsk.hh.ru" + element.attr("href");
                 webTarget = client.target(resumeLink);
                 resp = webTarget.request("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("user-agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .get();
                String resumeHtml = resp.readEntity(String.class);

                this.fileService.saveResume(Jsoup.parseBodyFragment(resumeHtml).getElementsByClass("resume-wrapper").html(), speciality.getName());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            findSpecialitiesUrl = "https://ulyanovsk.hh.ru/search/resume?clusters=True&area=98&order_by=relevance&logic=normal&pos=full_text&exp_period=all_time&ored_clusters=True&st=resumeSearch&page="+(page+1)+"&text=" + (new URLEncoder()).encode(speciality.getName(), StandardCharsets.UTF_8);
        }
    }
}
