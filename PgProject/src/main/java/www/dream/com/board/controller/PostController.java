package www.dream.com.board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import www.dream.com.board.model.PostVO;
import www.dream.com.board.service.PostService;
import www.dream.com.framework.model.Criteria;

@Controller
@RequestMapping("/post/*")
public class PostController {
	//함수 배치 순서는 목록, 상세, 생성, 수정, 삭제
	@Autowired
	private PostService postService;
	
	//HttpSession session를 추가한 이유는 csrf 처리가 session 완료 이후 생성되는 경우가 있고
	//이에 ERR_INCOMPLETE_CHUNKED_ENCODING 에러가 발생합니다. 이를 session 추가하였더니 사라졌습니다.
	@GetMapping("listPost")
	public void listPost(@RequestParam("boardId") long boardId, Criteria criteria, Model model, HttpSession session) {
		final long countTotal = postService.countTotalPostWithPaging(boardId, criteria);
		criteria.setTotalDataCount(countTotal);

		model.addAttribute("listPost", postService.listPostWithPaging(boardId, criteria));
		model.addAttribute("criteria", criteria);
		model.addAttribute("boardId", boardId);
	}
	
	/** 
	 * 게시글 상세 조회
	 */
	@GetMapping("postDetail")
	public void showPostDetail(@RequestParam("boardId") long boardId, @RequestParam("id") long id, 
			Criteria criteria, Model model, HttpSession session) {
		PostVO post = (PostVO) postService.findPostById(id);
		model.addAttribute("post", post);
		model.addAttribute("criteria", criteria);
		model.addAttribute("boardId", post.getBoardId());
	}

	/** 
	 * 게시글 등록
	 */
	@GetMapping("registerPost")
	@PreAuthorize("isAuthenticated()")	//로그인된 상태에 있음을 보장해 줍니다. 로그인 상태가 아니라면 로그인 페이지로 이동합니다.
	public void registerPost(@RequestParam("boardId") long boardId, Criteria criteria, Model model) {
		model.addAttribute("criteria", criteria);
		model.addAttribute("boardId", boardId);
	}
	@PostMapping("registerPost")
	@PreAuthorize("isAuthenticated()")
	public String registerPost(PostVO post, RedirectAttributes rttr) {
		postService.registerPost(post);
		rttr.addFlashAttribute("result", post.getId());
		rttr.addAttribute("boardId", post.getBoardId());
		return "redirect:/post/listPost";
	}

	/** 
	 * 게시글 수정 화면 연결
	 */
	@GetMapping("modifyPost")
	public String showModifyPost(@RequestParam("boardId") long boardId, @RequestParam("id") long id, 
			Criteria criteria, Model model, HttpSession session, RedirectAttributes rttr) {
		PostVO post = (PostVO) postService.findPostById(id);
		if (post != null) {
			model.addAttribute("post", post);
			model.addAttribute("criteria", criteria);
			model.addAttribute("boardId", post.getBoardId());
			return "/post/modifyPost";
		} else {
			/*만약 목록>상세>수정>삭제처리>목록  이 상태에서 브라우저에서 뒤로가기 버튼을 누르면 수정화면 띄워주세요가 요청됩니다.
			 * 이때 해당 게시글은 삭제가 된 상태이므로 post객체는 null 상태입니다. 이를 근거로 리다이렉트 처리합니다.
			 * 브라우저의 History 객체를 활용한 스크립트 처리는 완벽한 방안이 검색되지 않아서 리다이렉트로 방안을 결정하였습니다.
			*/
			rttr.addAttribute("pageNum", criteria.getPageNum());
			rttr.addAttribute("boardId", boardId);
			return "redirect:/post/listPost";
		}
	}
	/** 
	 * 게시글 수정 처리
	 */
	@PreAuthorize("principal.loginUser.id == #post.writer.id")
	@PostMapping("modifyPost")
	public String modifyPost(@RequestParam("boardId") long boardId, 
			@ModelAttribute("criteria") Criteria criteria, 
			PostVO post, RedirectAttributes rttr) {
		if (postService.updatePost(post)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("result", "success");
		rttr.addAttribute("pageNum", criteria.getPageNum());
		rttr.addAttribute("boardId", boardId);
		return "redirect:/post/listPost";
	}
	
	/** 
	 * 게시글 삭제
	 */
	@PreAuthorize("principal.loginUser.id == #post.writer.id")
	@PostMapping("removePost")
	public String removePost(@RequestParam("boardId") long boardId, Criteria criteria, PostVO post, RedirectAttributes rttr) {
		if (postService.removePost(post)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", criteria.getPageNum());
		rttr.addAttribute("boardId", boardId);
		return "redirect:/post/listPost";
	}
}
