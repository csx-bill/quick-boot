package com.quick.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.online.entity.Document;
import com.quick.online.entity.Request;
import com.quick.online.service.IDocumentService;
import com.quick.online.service.IRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/Document")
@RequiredArgsConstructor
@Tag(name = "APIJSON接口文档")
@PreAuth(replace = "Document:")
public class DocumentController extends SuperController<IDocumentService, Document, String> {

}
