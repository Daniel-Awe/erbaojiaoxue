package ${package.Controller};

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ${package.Entity}.${entity};
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${package.Service}.${table.serviceName};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.edu.zju.base.common.util.PageQuerySo;
import cn.edu.zju.base.common.util.ResultUtil;
import cn.edu.zju.base.common.util.Result;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;


/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(tags = "${table.comment!}")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${serviceRefName};

    @ApiOperation(value = "分页查询${table.comment!}")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public Result<Page<${entity}>> findPage(@RequestBody PageQuerySo<${entity}> pageQuerySo) {
        return ResultUtil.success(${serviceRefName}.page(pageQuerySo.initPage(), pageQuerySo.initQueryWrapper()));
    }

    @ApiOperation(value = "获取${table.comment!}")
    @RequestMapping(value = "/getDetailById", method = RequestMethod.GET)
    public Result getDetailById(@ApiParam(value = "id", required = true) @RequestParam Long id) {
        return ResultUtil.success(${serviceRefName}.getById(id));
    }

    @ApiOperation(value = "新增${table.comment!}")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody ${entity} dto) {
        return ResultUtil.success(${serviceRefName}.save(dto));
    }

    @ApiOperation(value = "修改${table.comment!}")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestBody ${entity} dto) {
        return ResultUtil.success(${serviceRefName}.updateById(dto));
    }

    @ApiOperation(value = "批量删除${table.comment!}")
    @RequestMapping(value = "/batchDel", method = RequestMethod.DELETE)
    public Result batchDel(@ApiParam(value = "目标id集合", required = true) @RequestParam(value = "ids") List<Long> ids) {
        return ResultUtil.success(${serviceRefName}.removeByIds(ids));
    }

    @ApiOperation(value = "删除${table.comment!}")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.DELETE)
    public Result deleteOne(
    @ApiParam(value = "目标id", required = true) @RequestParam Long id) {
        return ResultUtil.success(${serviceRefName}.removeById(id));
    }

}
</#if>
