package cn.edu.zju.temp.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 分页查询实体
 */
@Data
@ApiModel(value = "分页查询实体类")
@Slf4j
public class PageQuerySo<T> {

    @Schema(description = "分页大小", example = "10")
    protected long limit = 10L;

    @Schema(description = "当前页", example = "1")
    protected long page = 1L;

    @Schema(description = "实体参数", example = "")
    private T entity;

    public <W> Page<W> initPage() {
        return new Page<W>().setCurrent(page).setSize(limit);
    }

    public QueryWrapper<T> initQueryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.setEntity(entity);
        return wrapper;
    }

    public T tryGetEntity(Class<T> clazz) {
        if (entity == null) {
            try {
                return clazz.getConstructor().newInstance();
            } catch (Exception e) {
                log.error("尝试直接调用类{}的构造函数失败", clazz);
                return null;
            }
        } else {
            return entity;
        }
    }

}