package com.atipera.dto;

import java.util.List;

public class ResponseDTO {

    private int statusCode;
    private String message;
    private List<RepositoryDTO> repositoryDTOList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RepositoryDTO> getRepositoryDTOList() {
        return repositoryDTOList;
    }

    public void setRepositoryDTOList(List<RepositoryDTO> repositoryDTOList) {
        this.repositoryDTOList = repositoryDTOList;
    }
}
