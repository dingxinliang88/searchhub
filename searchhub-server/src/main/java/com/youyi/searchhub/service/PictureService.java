package com.youyi.searchhub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.dto.PictureQueryRequest;
import com.youyi.searchhub.model.vo.PictureVO;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public interface PictureService {

    Page<PictureVO> queryPictureByPage(PictureQueryRequest req);

}
