package com.academy.workSearch.service;

import com.academy.workSearch.model.enums.ProjectStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProjectStatusConverter implements AttributeConverter<ProjectStatus,String> {
    @Override
    public String convertToDatabaseColumn(ProjectStatus attribute) {
        if (attribute == null)
            return null;
        switch (attribute) {
            case LOOKING_FOR_WORKER:
                return "Looking for worker";
            case DEVELOPING:
                return "Developing";
            case TESTING:
                return "Testing";
            case CLOSED:
                return "Closed";

            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }
        @Override
        public ProjectStatus convertToEntityAttribute (String dbData){
            if (dbData == null)
                return null;
            switch (dbData) {
                case "Looking for worker":
                    return ProjectStatus.LOOKING_FOR_WORKER;
                case "Developing":
                    return ProjectStatus.DEVELOPING;
                case "Testing":
                    return ProjectStatus.TESTING;
                case "Closed":
                    return ProjectStatus.CLOSED;
                default:
                    throw new IllegalArgumentException(dbData + " not supported.");
            }
    }
}
