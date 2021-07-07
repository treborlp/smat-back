package com.inaigem.smatbackend.menu;

import com.inaigem.smatbackend.generic.ICRUD;

import java.util.List;


public interface IMenuService extends ICRUD<Menu, Integer> {
	
	List<Menu> listarMenuPorUsuario(String nombre);

}
