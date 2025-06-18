package com.flypiggyyoyoyo.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import com.flypiggyyoyoyo.demo.constants.SuccessEnum;
import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterRequest;
import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterResponse;
import com.flypiggyyoyoyo.demo.data.company.update.CompanyUpdateRequest;
import com.flypiggyyoyoyo.demo.exception.CompanyException;
import com.flypiggyyoyoyo.demo.exception.DatabaseException;
import com.flypiggyyoyoyo.demo.model.TbCompany;
import com.flypiggyyoyoyo.demo.service.TbCompanyService;
import com.flypiggyyoyoyo.demo.mapper.TbCompanyMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author flypiggy
 * @description 针对表【tb_company】的数据库操作Service实现
 * @createDate 2025-06-12 17:07:38
 */
@Service
public class TbCompanyServiceImpl extends ServiceImpl<TbCompanyMapper, TbCompany>
        implements TbCompanyService {

    // 企业规模映射表
    private static final Map<String, String> COMPANY_SIZE_MAP = new HashMap<>();
    // 企业性质映射表
    private static final Map<String, String> COMPANY_TYPE_MAP = new HashMap<>();
    // 企业状态映射表
    private static final Map<Integer, String> COMPANY_STATE_MAP = new HashMap<>();

    // 静态初始化映射表
    static {
        // 初始化企业规模映射
        COMPANY_SIZE_MAP.put("1", "100人以下");
        COMPANY_SIZE_MAP.put("2", "100-500人");
        COMPANY_SIZE_MAP.put("3", "500-1000人");
        COMPANY_SIZE_MAP.put("4", "1000-5000人");
        COMPANY_SIZE_MAP.put("5", "5000人以上");

        // 初始化企业性质映射
        COMPANY_TYPE_MAP.put("1", "国有企业");
        COMPANY_TYPE_MAP.put("2", "外资企业");
        COMPANY_TYPE_MAP.put("3", "合资企业");
        COMPANY_TYPE_MAP.put("4", "民营企业");
        COMPANY_TYPE_MAP.put("5", "股份制企业");
        COMPANY_TYPE_MAP.put("6", "上市公司");

        // 初始化企业状态映射
        COMPANY_STATE_MAP.put(1, "招聘中");
        COMPANY_STATE_MAP.put(2, "已暂停");
        COMPANY_STATE_MAP.put(3, "已结束");
    }

    @Override
    public CompanyRegisterResponse register(CompanyRegisterRequest request) {
        String companyName = request.getCompanyName();
        String companyArea = request.getCompanyArea();
        String companySize = request.getCompanySize();
        String companyType = request.getCompanyType();
        String companyBrief = request.getCompanyBrief();
        int companyState = request.getCompanyState();
        int companySort = request.getCompanySort();
        String companyPic = request.getCompanyPic();

        if (isRegister(companyName)) {
            throw new CompanyException(ErrorEnum.ENTERPRISE_ALREADY_EXIST);
        }

        System.out.println("验证成功");

        // 转换企业规模
        String companySizeText = COMPANY_SIZE_MAP.getOrDefault(companySize, companySize);

        // 转换企业性质
        String companyTypeText = COMPANY_TYPE_MAP.getOrDefault(companyType, companyType);

        // 转换企业状态（可选，取决于你的业务需求）
        // String companyStateText = COMPANY_STATE_MAP.getOrDefault(companyState, "未知状态");

        TbCompany company = new TbCompany()
                .setCompanyName(companyName)
                .setCompanyArea(companyArea)
                .setCompanySize(companySizeText)  // 设置转换后的文本
                .setCompanyType(companyTypeText)  // 设置转换后的文本
                .setCompanyBrief(companyBrief)
                .setCompanyState(companyState)    // 保持数字状态，或根据需要改为文本
                .setCompanySort(companySort)
                .setCompanyPic(companyPic);

        boolean isCompanySave = this.save(company);

        if (!isCompanySave) {
            throw new DatabaseException("数据库异常，存储信息失败");
        }

        CompanyRegisterResponse response = new CompanyRegisterResponse();
        response.setCode(SuccessEnum.REGISTER_SUCCESS.getCode());

        return response;
    }

    @Override
    public IPage<TbCompany> getCompanyPage(int pageNum, int pageSize, String name, String size) {
        // 创建分页对象
        Page<TbCompany> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<TbCompany> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("company_name", name);
        }
        if (size != null && !size.isEmpty()) {
            queryWrapper.eq("company_size", size);
        }

        // 添加排序条件
        queryWrapper.orderByAsc("company_sort");

        System.out.println("查询条件: " + queryWrapper.getSqlSegment());

        // 执行分页查询
        IPage<TbCompany> result = baseMapper.selectPage(page, queryWrapper);
        System.out.println("查询结果总数: " + result.getTotal());
        System.out.println("当前页记录数: " + result.getRecords().size());

        return result;
    }

    @Override
    public void updateCompany(CompanyUpdateRequest req) {
        // 1. 验证企业是否存在
        Long id = req.getCompanyId();
        TbCompany exist = this.getById(id);
        if (exist == null) {
            throw new CompanyException("企业不存在，ID=" + id);
        }

        // 2. 覆盖可更新字段（非空才覆盖）
        if (StringUtils.hasText(req.getCompanyName())) {
            exist.setCompanyName(req.getCompanyName());
        }
        if (StringUtils.hasText(req.getCompanyArea())) {
            exist.setCompanyArea(req.getCompanyArea());
        }
        if (StringUtils.hasText(req.getCompanySize())) {
            exist.setCompanySize(COMPANY_SIZE_MAP.getOrDefault(
                    req.getCompanySize(), req.getCompanySize()));
        }
        if (StringUtils.hasText(req.getCompanyType())) {
            exist.setCompanyType(COMPANY_TYPE_MAP.getOrDefault(
                    req.getCompanyType(), req.getCompanyType()));
        }
        if (StringUtils.hasText(req.getCompanyBrief())) {
            exist.setCompanyBrief(req.getCompanyBrief());
        }
        if (req.getCompanyState() != null) {
            exist.setCompanyState(req.getCompanyState());
        }
        if (req.getCompanySort() != null) {
            exist.setCompanySort(req.getCompanySort());
        }
        // 图片 URL 覆盖
        if (StringUtils.hasText(req.getCompanyPic())) {
            exist.setCompanyPic(req.getCompanyPic());
        }

        // 3. 执行更新
        boolean ok = this.updateById(exist);
        if (!ok) {
            throw new DatabaseException("数据库异常，更新企业信息失败");
        }
    }

    private boolean isRegister(String companyName) {
        QueryWrapper<TbCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("COMPANY_NAME", companyName);
        long count = this.count(queryWrapper);
        return count > 0;
    }
}