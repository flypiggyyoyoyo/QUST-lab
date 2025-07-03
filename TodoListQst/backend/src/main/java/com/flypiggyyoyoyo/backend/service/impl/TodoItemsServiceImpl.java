package com.flypiggyyoyoyo.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.backend.model.TodoItems;
import com.flypiggyyoyoyo.backend.service.TodoItemsService;
import com.flypiggyyoyoyo.backend.mapper.TodoItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author flypiggy
* @description 针对表【todo_items】的数据库操作Service实现
* @createDate 2025-07-03 19:53:46
*/
@Service
public class TodoItemsServiceImpl extends ServiceImpl<TodoItemsMapper, TodoItems>
    implements TodoItemsService{

}




