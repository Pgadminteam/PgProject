package www.dream.com.framework.attachFile.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import www.dream.com.framework.attachFile.model.AttachVO;
import www.dream.com.framework.attachFile.service.AttachFileService;

@Controller
public class AttachFileController {
	@Autowired
	private AttachFileService attachFileService;

	// 첨부 파일 목록 형식으로
	@GetMapping(value="/listAttach", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachVO>> listAttach(AttachVO attachVO) {
		List<AttachVO> listRet = new ArrayList<>();
		listRet = attachFileService.listAttach(attachVO);
		return new ResponseEntity<>(listRet, HttpStatus.OK);
	}

	/**
	 * Image 또는 Video에 대한 Thumbnail File을 img tag를 통하여 화면에 자동으로 출력해줍니다
	 * @param fileName absolute 한 full path
	 * @return
	 */
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
		File file = new File(fileName);
		ResponseEntity<byte[]> ret = null;
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			ret = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 일반 파일과 오디오에 대하여 지원해 줌.
	 * 이미지와 비디오는 다운로드 대상이 아님. @GetMapping("/display")를 보세요 
	 * @param userAgent
	 * @param fileName
	 * @return
	 */
	@GetMapping(value="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent,
			String fileName) {
		Resource resource = new FileSystemResource(fileName);
		if (!resource.exists())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		String resourceOriginalName = AttachVO.getOriginalName(fileName);
		HttpHeaders headers = new HttpHeaders();
		try {
			String downloadName = null;
			if (userAgent.contains("Trident")) {
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			} else if (userAgent.contains("Edge")) {
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
			} else {
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1");
			}
			headers.add("Content-Disposition",
					"attachment;filename=" + downloadName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	/**
	 * create 기능
	 * @param uploadFiles
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")	//로그인된 상태에 있음을 보장해 줍니다. 로그인 상태가 아니라면 로그인 페이지로 이동합니다.
	@PostMapping(value="/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachVO>> uploadAjaxAction(MultipartFile[] uploadFiles) {
		List<AttachVO> listRet = new ArrayList<>();
		
		File uploadPath = getUploadFolderMakeIfNotExist();

		for (MultipartFile aFile : uploadFiles) {
			//현 용도는 DTO
			AttachVO attachVO = new AttachVO(uploadPath, aFile);

			listRet.add(attachVO);
		}
		
		return new ResponseEntity<>(listRet, HttpStatus.OK);
	}

	/**
	 * 현재 날짜에 맞는 폴더가 없으면 현재 날짜로 폴더 만들어서 사용하기
	 */
	private File getUploadFolderMakeIfNotExist() {
		String uploadFolder = AttachVO.UPLOAD_FOLDER;
		File uploadPath = new File(uploadFolder, getFolder());
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		return uploadPath;
	}

	@PreAuthorize("isAuthenticated()")	//로그인된 상태에 있음을 보장해 줍니다. 로그인 상태가 아니라면 로그인 페이지로 이동합니다.
	@PostMapping(value="/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(AttachVO attachVO) {
		try {
			//업로드된 원본 지우기
			File file = new File(attachVO.getUploadedFileFullPath());
			file.delete();
			switch(attachVO.getFileType()) {
			case image:
			case video:
				//썸네일 지우기
				file = new File(attachVO.getFileCallPath());
				file.delete();
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
	}

	// 현재 날짜 불러와 그 날짜에 맞는 폴더 쓰기
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		// File.separator : unix 는 /요, Windows는 \니라
		return sdf.format(today).replace("-", File.separator);
	}
}
