package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id来查询对应的套餐
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量保存套餐和菜品的关联关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getDishesBySetmealId(Long id);

    /**
     * 根据套餐id删除相关菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void delete(Long id);

    /**
     * 根据套餐id批量删除相关菜品
     * @param setmealIds
     */
    void deleteByIds(List<Long> setmealIds);
}
