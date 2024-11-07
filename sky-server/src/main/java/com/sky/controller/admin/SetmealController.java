package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import com.sky.vo.SetmealVO;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO)
    {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result getMealById(@PathVariable Long id)
    {
        SetmealVO selmealVO = setmealService.getMealById(id);
        return Result.success(selmealVO);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) //删除所有setmealCache的key
    public Result update(@RequestBody SetmealDTO setmealDTO)
    {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("根据id删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) //删除所有setmealCache的key
    public Result deleteByIds(@RequestParam List<Long> ids)
    {
        setmealService.deleteByIds(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("起售或禁售套餐分类")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) //删除所有setmealCache的key
    public Result startOrStop(@PathVariable Integer status, @RequestParam Integer id)
    {
        setmealService.updateStatus(status, id);
        return Result.success();
    }
}
