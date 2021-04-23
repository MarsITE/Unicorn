package com.academy.workSearch.service.implementation;

import com.academy.workSearch.service.QuartzService;
import com.mchange.v1.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuartzServiceImpl implements QuartzService {
//    private final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);
//
//    public void memberStats() {
//        List<Member> members = memberRepository.findAll();
//        int activeCount = 0;
//        int inactiveCount = 0;
//        int registeredForClassesCount = 0;
//        int notRegisteredForClassesCount = 0;
//        for (Member member : members) {
//            if (member.isActive()) {
//                activeCount++;
//                if (CollectionUtils.isNotEmpty(member.getMemberClasses())) {
//                    registeredForClassesCount++;
//                } else {
//                    notRegisteredForClassesCount++;
//                }
//            } else {
//                inactiveCount++;
//            }
//        }
//        logger.info("Member Statics:");
//        logger.info("==============");
//        logger.info("Active member count: {}", activeCount);
//        logger.info(" - Registered for Classes count: {}", registeredForClassesCount);
//        logger.info(" - Not registered for Classes count: {}", notRegisteredForClassesCount);
//        logger.info("Inactive member count: {}", inactiveCount);
//        logger.info("==========================");
//    }
//
//    public void classStats() {
//        List<MemberClass> memberClasses = classRepository.findAll();
//        Map<String, Integer> memberClassesMap = memberClasses
//                .stream()
//                .collect(Collectors.toMap(MemberClass::getName, c -> 0));
//        List<Member> members = memberRepository.findAll();
//        for (Member member : members) {
//            if (CollectionUtils.isNotEmpty(member.getMemberClasses())) {
//                for (MemberClass memberClass : member.getMemberClasses()) {
//                    memberClassesMap.merge(memberClass.getName(), 1, Integer::sum);
//                }
//            }
//        }
//        logger.info("Class Statics:");
//        logger.info("=============");
//        memberClassesMap.forEach((k,v) -> logger.info("{}: {}", k, v));
//        logger.info("==========================");
//    }
}
