package cn.jeelearn.house.house.mapper;

import cn.jeelearn.house.house.model.City;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {
  
  public List<City> selectCitys(City city);

}