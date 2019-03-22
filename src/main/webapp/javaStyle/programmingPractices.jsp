<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <h1 id="s6_programming_practices">6. 程式碼慣例(Programming Practices)</h1>
<hr>
<p style="padding-left: 30px;"><strong>6.1 @Override: always used (<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s6.1-override-annotation" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s6.1_override_annotation">譯文</a>)</strong><br>
<strong>6.2 Caught exceptions: not ignored&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s6.2-caught-exceptions" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s6.2_caught_exceptions">譯文</a>)</strong><br>
<strong>6.3 Static members: qualified using class&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s6.3-static-members" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s6.3_static_members">譯文</a>)</strong><br>
<strong>6.4 Finalizers: not used&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s6.4-finalizers" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s6.4_finalizers">譯文</a>)</strong></p>
<h2 id="s6.1_override_annotation">6.1 @Override：一定要使用 (always used)</h2>
<hr>
<p>當一個被合法的標註了
			<span id="crayon-5c933e329eefd806837371" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@Override</span></span>&nbsp;的方法(method)，他一定是覆寫了其父類別 (superclass) 的方式、實作了介面方法(interface mthod) 以及介面中重新指定了其父介面 (superinterface) 的方法。</p>
<p>例外：
			<span id="crayon-5c933e329ef00846088115" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@Override</span></span>&nbsp;當可以被省略時，代表其父類別的方法已被標示為
			<span id="crayon-5c933e329ef02199263546" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@Deprecated</span></span></p>
<h2 id="s6.2_caught_exceptions">6.2 補獲異常：不可忽視 (Caught exceptions: not ignored)</h2>
<hr>
<p>下面這個異常 (Except) 描述，他是少數在發生異常時，可以不做回應的異常處理。(以標準的異常回應，是需要記錄下來的，或若是被視為「不可能(impoosible)」時，則重新用 
			<span id="crayon-5c933e329ef04148358176" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">AssertionError</span></span>&nbsp;&nbsp;拋出。</p>
<p>當在 catch 區塊中，確實沒有任何動作的話，便用註解明確的說明其原委。</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ef06692202721" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">try {
  int i = Integer.parseInt(response);
  return handleNumericResponse(i);
} catch (NumberFormatException ok) {
  // it's not numeric; that's fine, just continue
}
return handleTextResponse(response);</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ef06692202721-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef06692202721-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ef06692202721-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef06692202721-4">4</div><div class="crayon-num" data-line="crayon-5c933e329ef06692202721-5">5</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef06692202721-6">6</div><div class="crayon-num" data-line="crayon-5c933e329ef06692202721-7">7</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ef06692202721-1"><span class="crayon-st">try</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef06692202721-2"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-t">int</span><span class="crayon-h"> </span><span class="crayon-v">i</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-t">Integer</span><span class="crayon-sy">.</span><span class="crayon-e">parseInt</span><span class="crayon-sy">(</span><span class="crayon-v">response</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329ef06692202721-3"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-st">return</span><span class="crayon-h"> </span><span class="crayon-e">handleNumericResponse</span><span class="crayon-sy">(</span><span class="crayon-v">i</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef06692202721-4"><span class="crayon-sy">}</span><span class="crayon-h"> </span><span class="crayon-st">catch</span><span class="crayon-h"> </span><span class="crayon-sy">(</span><span class="crayon-e">NumberFormatException </span><span class="crayon-v">ok</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line" id="crayon-5c933e329ef06692202721-5"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-c">// it's not numeric; that's fine, just continue</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef06692202721-6"><span class="crayon-sy">}</span></div><div class="crayon-line" id="crayon-5c933e329ef06692202721-7"><span class="crayon-st">return</span><span class="crayon-h"> </span><span class="crayon-e">handleTextResponse</span><span class="crayon-sy">(</span><span class="crayon-v">response</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0005 seconds] -->
<p>例外：在測試中，若其 catch 的異常被命名為 
			<span id="crayon-5c933e329ef08340158521" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">expected</span></span>&nbsp;，則其註解可以被省略。下方這是一個常見的狀況，用以確保在測試時會拋出期望中的異常，所以這邊是不需要註解的。</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ef0a570899707" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">try {
  emptyStack.pop();
  fail();
} catch (NoSuchElementException expected) {
}</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ef0a570899707-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef0a570899707-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ef0a570899707-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef0a570899707-4">4</div><div class="crayon-num" data-line="crayon-5c933e329ef0a570899707-5">5</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ef0a570899707-1"><span class="crayon-st">try</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef0a570899707-2"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-v">emptyStack</span><span class="crayon-sy">.</span><span class="crayon-e">pop</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329ef0a570899707-3"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-e">fail</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef0a570899707-4"><span class="crayon-sy">}</span><span class="crayon-h"> </span><span class="crayon-st">catch</span><span class="crayon-h"> </span><span class="crayon-sy">(</span><span class="crayon-e">NoSuchElementException </span><span class="crayon-v">expected</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line" id="crayon-5c933e329ef0a570899707-5"><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0003 seconds] -->
<p></p>
<h2 id="s6.3_static_members">6.3 靜態成員：適當的搭配類別用&nbsp;(Static members: qualified using class)</h2>
<hr>
<p>引用靜態成員必需搭配著類別 (class) 名稱才是適當的用法，不是和一個物件類型或是描述句來使用。</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ef0c083696752" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">Foo aFoo = ...;
Foo.aStaticMethod(); // 好
aFoo.aStaticMethod(); // 不好
somethingThatYieldsAFoo().aStaticMethod(); // 非常糟</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ef0c083696752-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef0c083696752-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ef0c083696752-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef0c083696752-4">4</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ef0c083696752-1"><span class="crayon-e">Foo </span><span class="crayon-v">aFoo</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef0c083696752-2"><span class="crayon-v">Foo</span><span class="crayon-sy">.</span><span class="crayon-e">aStaticMethod</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span><span class="crayon-h"> </span><span class="crayon-c">// 好</span></div><div class="crayon-line" id="crayon-5c933e329ef0c083696752-3"><span class="crayon-v">aFoo</span><span class="crayon-sy">.</span><span class="crayon-e">aStaticMethod</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span><span class="crayon-h"> </span><span class="crayon-c">// 不好</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef0c083696752-4"><span class="crayon-e">somethingThatYieldsAFoo</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">.</span><span class="crayon-e">aStaticMethod</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span><span class="crayon-h"> </span><span class="crayon-c">// 非常糟</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0004 seconds] -->
<p></p>
<h2 id="s6.4_finalizers">6.4 Finalize 不要使用 (Finalizers: not used)</h2>
<hr>
<p>
			<span id="crayon-5c933e329ef0f628129545" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">Object.finalize</span></span>&nbsp;一個極為罕用到的覆寫方法。</p>
<p>[box&nbsp;style=”light-yellow info rounded”]&nbsp;<strong>Tip:</strong><br>
不要用他。若非用不可時，請先閱讀並確實理解 <a href="https://www.google.com/search?hl=zh-TW&amp;tbo=p&amp;tbm=bks&amp;q=isbn:8131726592" target="_blank">Effective Java</a> 的第七項：「Avoid Finalizers，」務必留心，並別這麼做。[/box]</p>