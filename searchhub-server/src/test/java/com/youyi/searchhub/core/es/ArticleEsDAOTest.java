package com.youyi.searchhub.core.es;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@SpringBootTest
class ArticleEsDAOTest {

    @Resource
    private ArticleEsDAO articleEsDAO;


    @Test
    void testAdd() {
        ArticleEsDTO articleEsDTO = new ArticleEsDTO();
        articleEsDTO.setId(1L);
        articleEsDTO.setTitle("youyi test");
        articleEsDTO.setContent("youyi channel, 冲冲冲，一切顺利");
        articleEsDTO.setCreateTime(new Date());
        articleEsDTO.setDeleted(0);

        articleEsDAO.save(articleEsDTO);

        log.info("article es dto id: {}", articleEsDTO.getId());
    }

    @Test
    void testSelect() {
        log.info("es data count: {}", articleEsDAO.count());
        Page<ArticleEsDTO> articlePage = articleEsDAO.findAll(
                PageRequest.of(0, 5, Sort.by("create_time")));
        List<ArticleEsDTO> articleList = articlePage.getContent();
        log.info("article list: {}", articleList);
        Optional<ArticleEsDTO> articleEsDtoOptional = articleEsDAO.findById(1L);
        log.info("articleEsDTO: {}", articleEsDtoOptional);
    }

    @Test
    void findByTitle() {
        List<ArticleEsDTO> articleEsDTOList = articleEsDAO.findByTitle("youyi test");
        log.info("articleEsDTOList: {}", articleEsDTOList);
    }
}