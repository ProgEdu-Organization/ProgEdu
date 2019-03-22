<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <h1 id="s7_javadoc">7. Javadoc</h1>
<hr>
<p style="padding-left: 30px;"><strong>7.1 Formatting (<a title="7.1 Formatting" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.1-javadoc-formatting" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.1_formatting">譯文</a>)</strong><br>
<strong>7.2 The summary fragment&nbsp;(<a title="7.2 The summary fragment" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.2-summary-fragment" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.2_the_summary_fragment">譯文</a>)</strong><br>
<strong>7.3 Where Javadoc is used&nbsp;(<a title="7.3 Where Javadoc is used" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.3-javadoc-where-required" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.3_where_javadoc_is_used">譯文</a>)</strong></p>
<h2 id="s7.1_formatting">7.1 格式 (Formatting)</h2>
<hr>
<p style="padding-left: 30px;"><strong>7.1.1 General form</strong><strong>&nbsp;(<a title="7.1.1 General form" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.1.1-javadoc-multi-line" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.1.1_javadoc_multi_line">譯文</a>)</strong><br>
<strong>7.1.2 Paragraphs</strong><strong>&nbsp;(<a title="7.1.2 Paragraphs" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.1.2-javadoc-paragraphs" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.1.2_javadoc_paragraphs">譯文</a>)</strong><br>
<strong>7.1.3 At-clauses</strong><strong>&nbsp;(<a title="7.1.3 At-clauses" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.1.3-javadoc-at-clauses" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.1.3_javadoc_at_clauses">譯文</a>)</strong></p>
<h3 id="s7.1.1_javadoc_multi_line">7.1.1 通用格式 (General form)</h3>
<hr>
<p>即本的 Javadoc 區塊格式如下範例：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ef11811699106" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">/**
 * Multiple lines of Javadoc text are written here,
 * wrapped normally...
 */
public int method(String p1) { ... }</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ef11811699106-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef11811699106-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ef11811699106-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ef11811699106-4">4</div><div class="crayon-num" data-line="crayon-5c933e329ef11811699106-5">5</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ef11811699106-1"><span class="crayon-c">/**</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef11811699106-2"><span class="crayon-c"> * Multiple lines of Javadoc text are written here,</span></div><div class="crayon-line" id="crayon-5c933e329ef11811699106-3"><span class="crayon-c"> * wrapped normally...</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ef11811699106-4"><span class="crayon-c"> */</span></div><div class="crayon-line" id="crayon-5c933e329ef11811699106-5"><span class="crayon-m">public</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-h"> </span><span class="crayon-e">method</span><span class="crayon-sy">(</span><span class="crayon-t">String</span><span class="crayon-h"> </span><span class="crayon-v">p1</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-h"> </span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-h"> </span><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0003 seconds] -->
<p>只有單行的範例：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ef13935536323" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">/** An especially short bit of Javadoc. */</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ef13935536323-1">1</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ef13935536323-1"><span class="crayon-c">/** An especially short bit of Javadoc. */</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0001 seconds] -->
<p>這是個永遠可以被接受的基本格式。當整個 Javadoc (包含註解) 沒用使用到 at-子句時，若是可以被容量在一行的話，就採用單行的格式。</p>
<h3 id="s7.1.2_javadoc_paragraphs">7.1.2 段落 (Paragraphs)</h3>
<hr>
<p>空行－在段落之間，以星號(<span style="color: #339966;">*</span>) 為起始的一行空白；若是有「at-子句」，就要將之擺在這個群組之前。若是每個段落在一開始有 <span style="color: #339966;">&lt;p&gt;</span> 時，他跟第一個字之間不會有空格。</p>
<h3 id="s7.1.3_javadoc_at_clauses">7.1.3 At-子句 (At-clauses)</h3>
<hr>
<p>當全部的「at-子句 」都出現時，其標準的使用順序為 
			<span id="crayon-5c933e329ef15642131645" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@param</span></span>&nbsp;、
			<span id="crayon-5c933e329ef18986662139" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@return</span></span>&nbsp;、
			<span id="crayon-5c933e329ef1a938979729" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@throws</span></span>&nbsp;、
			<span id="crayon-5c933e329ef1c908960250" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@deprecated</span></span>&nbsp;，而這四種類型都不會為空。當一個「at-子句」無法以單行描述完畢時，其續行該要以 <span style="color: #339966;">@</span> 為基準做四個(或更多)空白的縮排。</p>
<h2 id="s7.2_the_summary_fragment">7.2 摘要片段 (The summary fragment)</h2>
<hr>
<p>每個類別 (class) 以及成員 (member) 的 Javadoc，都先以簡潔的摘要片段做為開始。這個片段非常地重要：在某些狀況下，他會是該類別 (class) 或是 方法 (method) 索引中，唯一出現的文字片段。</p>
<p>片段 － 一個名詞片語或是動詞片語，不是一個完整的句子。他<strong>不會</strong>是這麼開始「<span style="color: #ff00ff;">&nbsp;A {@code Foo} is a …</span>」，或是「&nbsp;<span style="color: #ff00ff;">This method returns …</span>」，也不會是一個完整祈使句「<span style="color: #ff00ff;">Save the record …</span>」。然而，因為開頭大寫與標點符號的關該，讓片段看起來就像是一個完整的句子。</p>
<p>[box style=”light-yellow info rounded”] <strong>Tip:</strong><br>
舉一個 Javadoc 格式中錯用符號為例 <span style="color: #ff00ff;">/** @return the customer ID */</span>。這需要被修正成 <span style="color: #993300;">/** Returns the customer ID. */</span>[/box]</p>
<h2 id="s7.3_where_javadoc_is_used">7.3 在哪裡使用 Javadoc (Where Javadoc is used)</h2>
<hr>
<p style="padding-left: 30px;"><strong>7.3.1 Exception: self-explanatory methods</strong><strong>&nbsp;(<a title="7.3.1 Exception: self-explanatory methods" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.3.1-javadoc-exception-self-explanatory" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.3.1_javadoc_exception_self_explanatory">譯文</a>)</strong><br>
<strong>7.3.2 Exception: overrides</strong><strong>&nbsp;(<a title="7.3.2 Exception: overrides" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s7.3.2-javadoc-exception-overrides" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s7.3.2_javadoc_exception_overrides">譯文</a>)</strong></p>
<p>在最小的限度下，所有公開類別 (public class) 以及每個類別 (class) 中公開 (public) 或被保護的(protected) 成員都該寫上 Javadoc，當然也有少數如下列出的例外狀況。</p>
<p>若是被拿來做為定義類別 (class)、方法 (method) 或是屬性 (field) 在實作時該有的整體目的或是行為，就該寫上註解 (comment) 而非 Javadoc。(這樣做將更趨一致性並更讓工具看起來親切些 (tool-friendly))。</p>
<p>[box style=”lavender announcement&nbsp;rounded” ]<strong>譯注：<br>
</strong>tool-friendly 不知道怎麼翻譯比較好，所以就用比較直白的陳述表達了。[/box]</p>
<h3 id="s7.3.1_javadoc_exception_self_explanatory">7.3.1 例外：不言自明的方法 (Exception: self-explanatory methods)</h3>
<hr>
<p>有些「簡單、明確」的方法也不一定要寫上 Javadoc，像是&nbsp;
			<span id="crayon-5c933e329ef1e256881473" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">getFoo</span></span>&nbsp;這種簡明的案例，好像除了寫上「返回 foo 值」也什麼好寫的。</p>
<p>[box &nbsp;style=”blue info rounded”&nbsp;]<strong>Important:</strong><br>
當今天是一個必需要讓看的人知道的狀況下，這個例外就不該拿出來做為忽略不寫相關資訊的理由。比方說，有一個方法被命名為 
			<span id="crayon-5c933e329ef20671694702" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">getCanonicalName</span></span>，這種狀況下，看得人可能並不了解「canonical name」這是什麼意思，在這種狀況下就不要忽略它的說明(包含只寫上「返回 canonical name」)。 [/box]</p>
<h3 id="s7.3.2_javadoc_exception_overrides">7.3.2 例外：覆寫&nbsp;(Exception: overrides)</h3>
<hr>
<p>在子物中覆寫的父類別的方法就不一定要寫上&nbsp;Javadoc。</p>