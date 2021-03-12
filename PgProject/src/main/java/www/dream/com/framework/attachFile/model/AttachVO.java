package www.dream.com.framework.attachFile.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.annotations.Expose;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnailator;

@Data
public class AttachVO implements Serializable {


	//여러 멀티미디어 파일 타입을 통합적으로 판단합니다.
	public enum FileTypes {
		image("i_"), video("v_"), audio, others;
		private String thumnailFileHeader = null;
		private FileTypes() {}		
		private FileTypes(String thumnailFileHeader) {
			this.thumnailFileHeader = thumnailFileHeader;
		}	
		public static FileTypes findMappedElement(String contentType) {
			for (FileTypes enumEle : FileTypes.values()) {
				if (contentType.startsWith(enumEle.toString())) {
					return enumEle;
				}
			}
			return others;
		}
		public String makeThumdnailFileName(String serverFileName) {
			return thumnailFileHeader + serverFileName;
		}
	}
	
	public static final String UPLOAD_FOLDER = "C:\\upload";
	private static final String UUID_SEPERATOR = "_";

	private static final int THUMBNAIL_IMG_WIDTH = 100;
	private static final int THUMBNAIL_IMG_HEIGHT = 100;

	/** Table Mapping 영역 */
	private String ownerType;
	private long ownerId;
	@Expose
	private String uuid;
	@Expose
	private String uploadPath;
	@Expose
	private String fileName;
	@Expose
	private String uploadedThumdnailFileName;
	//Enum 형식으로 만들어짐
	@Expose
	private FileTypes fileType;

	/** DTO관점에서 추가적으로 필요한 속성 영역 */
	@Expose
	private String originalFilename;

	@Expose
	private String fileCallPath;

	public AttachVO() {
		int i = 0;
		i++;
	}

	public AttachVO(AttachVO vo) {
		uploadPath = vo.uploadPath;
		originalFilename = vo.originalFilename;
		uuid = vo.uuid;
		fileName = vo.fileName;
	}
	
	public AttachVO(String uuid, String ownerType, long ownerId) {
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.uuid = uuid;
	}

	public AttachVO(File uploadFolder, MultipartFile uploadFile) {
		this.uploadPath = uploadFolder.getAbsolutePath();

		String uploadFilename = uploadFile.getOriginalFilename();
		// IE에서 넘어올 때는 originalFilename에 절대경로가 모두 포함되어 들어오므로 맨 뒤의 순수 파일이름만 취합니다.
		uploadFilename = uploadFilename.substring(uploadFilename.lastIndexOf("\\") + 1);
		this.originalFilename = uploadFilename;

		this.uuid = UUID.randomUUID().toString();

		this.fileName = this.uuid + UUID_SEPERATOR + this.originalFilename;
		
		try {
			File saveFile = new File(this.uploadPath, this.fileName);

			uploadFile.transferTo(saveFile);	//Client file을 서버 해당 위치로 Upload
			this.fileType = checkFileType(saveFile);
			createThumbNail(saveFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createThumbNail(File savedFile) throws Exception {
		String thumbnailTemp = this.fileName.substring(0, this.fileName.lastIndexOf('.')) + ".png";
		if (fileType == FileTypes.image) {
			this.uploadedThumdnailFileName = fileType.makeThumdnailFileName(thumbnailTemp);
			InputStream inputStream = new FileInputStream(savedFile);
			createThumbNailInternal(inputStream);
		} else if (fileType == FileTypes.video) {
			this.uploadedThumdnailFileName = fileType.makeThumdnailFileName(thumbnailTemp);
			//VideoFrame을 File로 저장하지 않고 inputStream을 활용하여 
			//Thumbnailator.createThumbnail로 입력되도록 Pipelining 구조로 개발함
			InputStream inputStreamOfVideoFram = getThumbnailFromVideoFrame(savedFile);
			createThumbNailInternal(inputStreamOfVideoFram);
		}
	}
	/* 조건문(if, switch, 삼항연산자)으로 길게 구사하는 함수들은 
	 * 클래스 상속의 함수 Override를 통하여 재구조화 가능합니다. */
	private void createThumbNailInternal(InputStream inputStream) {
		try {
			File thumbnailFile = new File(uploadPath, this.uploadedThumdnailFileName);
			FileOutputStream outStream = new FileOutputStream(thumbnailFile);

			Thumbnailator.createThumbnail(inputStream, outStream, THUMBNAIL_IMG_WIDTH, THUMBNAIL_IMG_HEIGHT);
			inputStream.close();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private InputStream getThumbnailFromVideoFrame(File source) throws IOException, JCodecException {
		int frameNumber = 0;
		//만들어진 Thumbnail 크기가 생각보다는 상당하였음
		Picture picture = FrameGrab.getFrameFromFile(source, frameNumber);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public String getFileCallPath() {
		StringBuilder sb = new StringBuilder(uploadPath);
		sb.append(File.separator);
		switch(fileType) {
		case image:
		case video:
			sb.append(uploadedThumdnailFileName);
			break;
		case audio:
		case others:
			sb.append(fileName);
			break;
		}
		return sb.toString();
	}
	/**
	 * 업로드된 원본 파일을 Image 또는 Video로 화면에 띄워주기 위한 경로 제공 기능
	 * @return
	 */
	public String getUploadedFileFullPath() {
		StringBuilder sb = new StringBuilder(uploadPath);
		sb.append(File.separator);
		sb.append(fileName);
		return sb.toString();
	}

	public static String getOriginalName(String fileName) {
		return fileName.substring(fileName.lastIndexOf(UUID_SEPERATOR) + 1);
	}

	private static FileTypes checkFileType(File file) {
		String contentType = null;
		try {
			contentType = Files.probeContentType(file.toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FileTypes.findMappedElement(contentType);
	}
}
