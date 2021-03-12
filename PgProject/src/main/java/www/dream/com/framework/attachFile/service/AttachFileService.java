package www.dream.com.framework.attachFile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.dream.com.framework.attachFile.model.AttachVO;
import www.dream.com.framework.attachFile.model.mapper.AttachMapper;

@Service
public class AttachFileService {
	@Autowired
	private AttachMapper attachMapper;

	public List<AttachVO> listAttach(AttachVO ownerIdAndType) {
		return attachMapper.listAttach(ownerIdAndType);
	}

}
