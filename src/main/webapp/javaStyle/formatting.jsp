<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<h1 id="s4_formatting">4. 格式(Formatting)</h1>
<hr>
<p style="padding-left: 30px;"><strong>4.1 Braces (<a title="4.1 Braces" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.1-braces" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.1_braces">譯文</a>)</strong><br>
<strong>4.2 Block indentation: +2 spaces&nbsp;(<a title="4.2 Block indentation: +2 spaces" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.2-block-indentation" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.2_block_indentation">譯文</a>)</strong><br>
<strong>4.3&nbsp;One statement per line&nbsp;(<a title="4.3 One statement per line" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.3-one-statement-per-line" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.3_one_statement_per_line">譯文</a>)</strong><br>
<strong>4.4&nbsp;Column limit: 80 or 100&nbsp;(<a title="4.4 Column limit: 80 or 100" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.4-column-limit" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.4_column_limit">譯文</a>)</strong><br>
<strong>4.5&nbsp;Line-wrapping&nbsp;(<a title="4.5 Line-wrapping" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.5-line-wrapping" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.5_line_wrapping">譯文</a>)</strong><br>
<strong>4.6&nbsp;Whitespace&nbsp;(<a title="4.6 Whitespace" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.6-whitespace" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.6_whitespace">譯文</a>)</strong><br>
<strong>4.7&nbsp;Grouping parentheses: recommended&nbsp;(<a title="4.7 Grouping parentheses: recommended" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.7-grouping-parentheses" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.7_grouping_parentheses">譯文</a>)</strong><br>
<strong>4.8&nbsp;Specific constructs&nbsp;(<a title="4.8 Specific constructs" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8-specific-constructs" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8_specific_constructs">譯文</a>)</strong></p>
<p><strong>術語說明：</strong>類別中的建構函式 (constructor) 或是函式 (method) 該以區塊結構(block-like construct) 呈現。特別是像 4.8.3.1 裡陣例的初始值，所有陣列的初始值只要是以區塊結構呈現，並不限制其格式都要一樣。</p>
<h2 id="s4.1_braces">4.1 大括號 (Braces)</h2>
<hr>
<p style="padding-left: 30px;"><strong>4.1.1 Braces are used where optional</strong><strong>&nbsp;(<a title="4.1.1 Braces are used where optional" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.1.1-braces-always-used" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.1.1_braces_are_used_where_optional">譯文</a>)</strong><br>
<strong>4.1.2 Nonempty blocks: K &amp; R style</strong><strong>&nbsp;(<a title="4.1.2 Nonempty blocks: K &amp; R style" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.1.2-blocks-k-r-style" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.1.2_nonempty_blocks">譯文</a>)</strong><br>
<strong>4.1.3 Empty blocks: may be concise</strong><strong>&nbsp;(<a title="4.1.3 Empty blocks: may be concise" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.1.3-braces-empty-blocks" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.1.3_empty_blocks">譯文</a>)</strong></p>
<h3 id="s4.1.1_braces_are_used_where_optional">4.1.1 即便是有選擇性的狀況，都要使用大括號 (Braces are used where optional)</h3>
<hr>
<p>在&nbsp;
			<span id="crayon-5c933e329ee2b838157418" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">if</span></span>、
			<span id="crayon-5c933e329ee2d355637399" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">else</span></span>、
			<span id="crayon-5c933e329ee30632413834" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">for</span></span>、
			<span id="crayon-5c933e329ee32637149990" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">do</span></span>&nbsp;以及
			<span id="crayon-5c933e329ee34593017472" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">while</span></span>&nbsp;這些敘述裡的程式碼，就算是空的，或是僅有一行，也都要使用大括號。</p>
<h3 id="s4.1.2_nonempty_blocks">4.1.2 非空區塊 (Nonempty blocks: K &amp; R style)</h3>
<hr>
<p>在內容為非空的區塊結構中，大括號需依序著 Kernighan and Ritchie 風格 (<a title="Egyptian Brackets" href="http://blog.codinghorror.com/new-programming-jargon/" target="_blank">Egyptian Brackets</a>)，如下</p>
<ul>
<li>左括號前不要換行</li>
<li>在左括號後換行</li>
<li>在右括號後換行</li>
<li>當右括號是用在一個敘述語法、函式、建構函式或是類別之後，就要換行。若是像其後接續的是 
			<span id="crayon-5c933e329ee36423138399" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">else</span></span>&nbsp;&nbsp;或是逗號就不用。</li>
</ul>
<p>範例：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ee38271625505" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">return new MyClass() {
  @Override public void method() {
    if (condition()) {
      try {
        something();
      } catch (ProblemException e) {
        recover();
      }
    }
  }
};</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ee38271625505-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee38271625505-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ee38271625505-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee38271625505-4">4</div><div class="crayon-num" data-line="crayon-5c933e329ee38271625505-5">5</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee38271625505-6">6</div><div class="crayon-num" data-line="crayon-5c933e329ee38271625505-7">7</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee38271625505-8">8</div><div class="crayon-num" data-line="crayon-5c933e329ee38271625505-9">9</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee38271625505-10">10</div><div class="crayon-num" data-line="crayon-5c933e329ee38271625505-11">11</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ee38271625505-1"><span class="crayon-st">return</span><span class="crayon-h"> </span><span class="crayon-r">new</span><span class="crayon-h"> </span><span class="crayon-e">MyClass</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee38271625505-2"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-n">@Override</span><span class="crayon-h"> </span><span class="crayon-m">public</span><span class="crayon-h"> </span><span class="crayon-t">void</span><span class="crayon-h"> </span><span class="crayon-e">method</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line" id="crayon-5c933e329ee38271625505-3"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-st">if</span><span class="crayon-h"> </span><span class="crayon-sy">(</span><span class="crayon-e">condition</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee38271625505-4"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-st">try</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line" id="crayon-5c933e329ee38271625505-5"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-e">something</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee38271625505-6"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-sy">}</span><span class="crayon-h"> </span><span class="crayon-st">catch</span><span class="crayon-h"> </span><span class="crayon-sy">(</span><span class="crayon-i">ProblemException</span><span class="crayon-h"> </span><span class="crayon-v">e</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line" id="crayon-5c933e329ee38271625505-7"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-e">recover</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee38271625505-8"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-sy">}</span></div><div class="crayon-line" id="crayon-5c933e329ee38271625505-9"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-sy">}</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee38271625505-10"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-sy">}</span></div><div class="crayon-line" id="crayon-5c933e329ee38271625505-11"><span class="crayon-sy">}</span><span class="crayon-sy">;</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0008 seconds] -->
<p>但仍是有例外，像是列舉類別，請見 4.8.1。</p>
<h3 id="s4.1.3_empty_blocks">4.1.3 為空的區塊：使其簡潔 (Empty blocks: may be concise)</h3>
<hr>
<p>當一個區塊結構為空時，可以將左右括號寫在一起，不需要在其中加入空白或是換行 (
			<span id="crayon-5c933e329ee3a410409584" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">{}</span></span>) 除非他是多區塊敘述語法的其中一部份(如 
			<span id="crayon-5c933e329ee3d389551081" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">if/else-if/else</span></span>&nbsp;或是
			<span id="crayon-5c933e329ee3f124933953" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">try/catch/finally</span></span>&nbsp;)。</p>
<p>範例：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ee41784291935" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">void doNothing() {}</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ee41784291935-1">1</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ee41784291935-1"><span class="crayon-t">void</span><span class="crayon-h"> </span><span class="crayon-e">doNothing</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0002 seconds] -->
<p></p>
<h2 id="s4.2_block_indentation">4.2 縮排：+2 個空白&nbsp;(Block indentation: +2 spaces)</h2>
<hr>
<p>每新增一個區塊結構時，便要在其開始就增加兩個空白。當區塊結束時，其縮排則返回到和前一層的縮排一致。而縮排的層級適用於其間的程式碼與註解。(範例請見 4.1.2)</p>
<h2 id="s4.3_one_statement_per_line">4.3 每行一敘述 (One statement per line)</h2>
<hr>
<p>每行的敘述都要換行。</p>
<h2 id="s4.4_column_limit">4.4 每列字數限制：80 或 100 (Column limit: 80 or 100)</h2>
<hr>
<p>每個專案皆可選擇自己要以每列&nbsp;80 或是 100 個字元為限制 。除非是遇到下述例外，否則每行的字數當達到限制時就需要換行，換行的說明請見 4.5 Line-wrapping。</p>
<p><strong>例外：</strong></p>
<ol>
<li>不可能滿足在一列的字數限制的條件 (比方說，在 Javadoc 裡很長 URL，或是參考了一個有很長名稱的 JSNI 函式)。</li>
<li>
			<span id="crayon-5c933e329ee43787226984" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">package</span></span>以及
			<span id="crayon-5c933e329ee45590371284" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">import</span></span>敘述 (請見 3.2 Package 敘述 以及 3.3 Import 敘述區)。</li>
<li>註解中，可能會被複製、貼上到 shell 的指令</li>
</ol>
<h2 id="s4.5_line_wrapping">4.5 換行 (Line-wrapping)</h2>
<hr>
<p style="padding-left: 30px;"><strong>4.5.1 Where to break</strong><strong>&nbsp;(<a title="4.5.1 Where to break;" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.5.1-line-wrapping-where-to-break" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.5.1_where_to_break">譯文</a>)</strong><br>
<strong>4.5.2 Indent continuation lines at least +4 spaces</strong><strong>&nbsp;(<a title="4.5.2 Indent continuation lines at least +4 spaces" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.5.2-line-wrapping-indent" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.5.2_indent_continuation_lines">譯文</a>)</strong></p>
<p><strong>術語說明：</strong>當程式碼可能在單行裡超過每列限制，而將之拆開到多行中，這個動作稱之為換行 (Line-wrapping)。</p>
<p>並沒有一種方程式可以完整、明確的告訴我們在所有情況中該如何換行。而有常有多種合理的方式去對同一段程式碼換行。</p>
<p>[box style=”light-yellow info&nbsp;rounded” ] <strong>Tip:</strong><br>
濃縮函式或是區域變數，也許可以解決這種需要被換行的情形。[/box]</p>
<h3 id="s4.5.1_where_to_break">4.5.1 哪裡該斷開 (Where to break)</h3>
<hr>
<p>換行的主要原則：傾向在進級的語法 (higher syntactic level) 斷開。還有，</p>
<ol>
<li>當該行最末的符號若非賦值的等號，則在該符號前換行。(請注意，這裡和其他實作成&nbsp;Google 風格的程式語言，如 C++ 以及 JavaScript 不同。)
<ul>
<li>這也適用於「類運算 (operator-like)」符號：如點分格符號 (
			<span id="crayon-5c933e329ee47688532475" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">.</span></span>&nbsp;)，或是像類別限制中的 &amp; 符號 (
			<span id="crayon-5c933e329ee4a773183424" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">&lt;T extends Foo &amp; Bar&gt;</span></span>&nbsp;)，以及 catch 區塊 (
			<span id="crayon-5c933e329ee4c973023115" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">catch (FooException | BarException e)</span></span>&nbsp;)。</li>
</ul>
</li>
<li>當一行是要斷在賦值的等號時，平常的狀況就是將其斷在該等號之後，但反之也是可以接受的。
<ul>
<li>這也適用在 
			<span id="crayon-5c933e329ee4e034151550" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">for</span></span>&nbsp;的擴充型 「foreach」所用的冒號，這種「類等號」(assignment-operator-like) 符號。</li>
</ul>
</li>
<li>左括號 (
			<span id="crayon-5c933e329ee50862598257" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">(</span></span>) 與函式或是建構函式名留在同一行。</li>
<li>逗號 (
			<span id="crayon-5c933e329ee53469715128" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">,</span></span>) 與其前方的部份留在同行。</li>
</ol>
<h3 id="s4.5.2_indent_continuation_lines">4.5.2 每行縮排至少要有一組( 4 個)空白&nbsp;(Indent continuation lines at least +4 spaces)</h3>
<hr>
<p>每個接續在斷行後的第一行開始，都要比原行加上一組( 4 個)以上的空白來縮排。</p>
<p>當有連續多行時，其縮排可能會超過一組( 4 個)空白。一般來說，若是連續的兩行都是斷於同級別語法元素時，其縮排階層會是相同的。</p>
<h2 id="#s4.6_whitespace">4.6 Whitespace</h2>
<hr>
<p style="padding-left: 30px;"><strong>4.6.1 Vertical whitespace</strong><strong>&nbsp;(<a title="4.6.1 Vertical whitespace" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.6.1-vertical-whitespace" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.6.1_vertical_whitespace">譯文</a>)</strong><br>
<strong>4.6.2 Horizontal whitespace</strong><strong>&nbsp;(<a title="4.6.2 Horizontal whitespace" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.6.2-horizontal-whitespace" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.6.2_horizontal_whitespace">譯文</a>)</strong><br>
<strong>4.6.3 Horizontal alignment: never required</strong><strong>&nbsp;(<a title="4.6.3 Horizontal alignment: never required" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.6.3-horizontal-alignment" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.6.3_horizontal_alignment">譯文</a>)</strong></p>
<h3 id="s4.6.1_vertical_whitespace">4.6.1 垂直空白 (Vertical whitespace)</h3>
<hr>
<p>使用一空行的情形：</p>
<ol>
<li>在類別中接連在一起的成員(或是初始值)間：屬性、建構函式、函式、巢狀類別、靜能初始區塊以及實作初始區塊。
<ul>
<li><strong>例外</strong>：兩個接連在一起的屬性間可以考慮使用空行 (沒有其他程式碼在其之間)。這是為了對屬性進行邏輯分組。</li>
</ul>
</li>
<li>在函式區塊裡，需要對程式敘述進行邏輯分組。</li>
<li>在類別中第一個成員前或是最後一個成員後(既不鼓勵也不反動)。</li>
<li>在文字描述間有分章節的需求 (如 3.3 Import statements)。</li>
</ol>
<p>一行中出現連續多個空白是被允許的，但從沒有這種需求 (或是鼓勵)。</p>
<h3 id="s4.6.2_horizontal_whitespace">4.6.2 水平空白 (Horizontal whitespace)</h3>
<hr>
<p>除了語言需求或是風格規則，還有文字、註解、Javadoc 與單一 ASCII 空白，也僅出現在下列狀況：</p>
<ol>
<li>隔開與任意保留字同行並緊鄰於後的左括號 (
			<span id="crayon-5c933e329ee55236714465" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">(</span></span>)，如&nbsp;
			<span id="crayon-5c933e329ee57433270388" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">if</span></span>&nbsp;、
			<span id="crayon-5c933e329ee59962589882" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">for</span></span>&nbsp;或&nbsp;
			<span id="crayon-5c933e329ee5b072468440" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">catch</span></span>&nbsp;。</li>
<li>隔開與任意保留字同行並緊鄰於前的右大括號 (
			<span id="crayon-5c933e329ee5d678438659" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">}</span></span>&nbsp;)，如&nbsp;
			<span id="crayon-5c933e329ee60217359817" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">else</span></span>&nbsp;或&nbsp;
			<span id="crayon-5c933e329ee62130725093" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">catch</span></span>&nbsp;。</li>
<li>所有左大括號(
			<span id="crayon-5c933e329ee64130244885" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">{</span></span>&nbsp;)之前，有兩個例外：
<ul>
<li>
			<span id="crayon-5c933e329ee66874364800" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">@SomeAnnotation({a, b})</span></span>&nbsp;&nbsp;(不用空白)</li>
<li>
			<span id="crayon-5c933e329ee68821665369" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">String[][] x = {{"foo"}};</span></span>&nbsp;&nbsp;(
			<span id="crayon-5c933e329ee6a561020100" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">{{</span></span>之間不用空白，請見第 8 點下的 Note)</li>
</ul>
</li>
<li>在二或三元運算子的兩側。也適用於下方提到的「類運算子 (operator-like)」符號：
<ul>
<li>類別限制中的 &amp; 符號&nbsp;
			<span id="crayon-5c933e329ee6c121112602" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">&lt;T extends Foo &amp; Bar&gt;</span></span></li>
<li>catch 區塊
			<span id="crayon-5c933e329ee6e005827904" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">catch (FooException | BarException e)</span></span></li>
<li>
			<span id="crayon-5c933e329ee71308211340" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">for</span></span>的擴充型敘述「foreach」中的冒號 (
			<span id="crayon-5c933e329ee73648709653" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">:</span></span>&nbsp;)</li>
</ul>
</li>
<li>在
			<span id="crayon-5c933e329ee75982933195" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">,:;</span></span>&nbsp;之後的右括號(
			<span id="crayon-5c933e329ee77059082508" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">)</span></span>&nbsp;)。</li>
<li>註解用在某行的最後，其開始的雙斜線兩側。在這裡允許多個空白，但沒有要求。</li>
<li>宣告的類別與變數之間：
			<span id="crayon-5c933e329ee79136270417" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">List&lt;String&gt; list</span></span>&nbsp;。</li>
<li>陣列初始值用的大括號中可使用
<ul>
<li>
			<span id="crayon-5c933e329ee7b875370540" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">new int[] {5, 6}</span></span>以及 
			<span id="crayon-5c933e329ee7e964538143" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">new int[] { 5, 6 }</span></span>&nbsp;兩種皆可。</li>
</ul>
</li>
</ol>
<p>[box style=”blue info&nbsp;rounded” ]<strong>Important:</strong><br>
這個規則從未要求或是禁止增加的空白在一行的開始或是結束，只對在裡面的空白。[/box]</p>
<h3 id="s4.6.3_horizontal_alignment">4.6.3 水平對齊：絕不會特別要求&nbsp;(Horizontal alignment: never required)</h3>
<hr>
<p><strong>術語說明：</strong>水平對齊主要的目的，是增加空白來讓程式碼在某一行裡的內容跟上一行相對應的內容對齊。</p>
<p>這做法是被允許的，但他不屬於 Google 風格。這甚至在已經使用的地方，也沒有被要求修改掉。</p>
<p>這邊舉個例子分別呈現沒有對齊，以及用此方法對齊：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ee80746396303" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">private int x; // 這個沒問題
private Color color; // 這也是

private int   x;      // 允計，但未來
private Color color;  // 可以將他改為不對齊</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ee80746396303-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee80746396303-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ee80746396303-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee80746396303-4">4</div><div class="crayon-num" data-line="crayon-5c933e329ee80746396303-5">5</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ee80746396303-1"><span class="crayon-m">private</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-h"> </span><span class="crayon-v">x</span><span class="crayon-sy">;</span><span class="crayon-h"> </span><span class="crayon-c">// 這個沒問題</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee80746396303-2"><span class="crayon-m">private</span><span class="crayon-h"> </span><span class="crayon-e">Color </span><span class="crayon-v">color</span><span class="crayon-sy">;</span><span class="crayon-h"> </span><span class="crayon-c">// 這也是</span></div><div class="crayon-line" id="crayon-5c933e329ee80746396303-3">&nbsp;</div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee80746396303-4"><span class="crayon-m">private</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-h">&nbsp;&nbsp; </span><span class="crayon-v">x</span><span class="crayon-sy">;</span><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-c">// 允計，但未來</span></div><div class="crayon-line" id="crayon-5c933e329ee80746396303-5"><span class="crayon-m">private</span><span class="crayon-h"> </span><span class="crayon-e">Color </span><span class="crayon-v">color</span><span class="crayon-sy">;</span><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-c">// 可以將他改為不對齊</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0004 seconds] -->
<p>[box style=”light-yellow info&nbsp;rounded” ] <strong>Tip:</strong><br>
對齊是可以增加其可讀性，但他會為日後的維護帶來麻煩。若是在未來要更動其中的一行，就會弄亂原本的格式，而且這也是被允許的。更多時候他會提醒程式人員(也許就是你)去將該行附近的空白調整好，而引發一連串的重構。就因改了那一行而產生的「連鎖反應」。這樣的最糟狀況就是瞎忙，不但影響了歷史資訊，還減慢了程式碼審查者的效率並造成更多合併時的衝突。[/box]</p>
<h2 id="s4.7_grouping_parentheses">4.7 用小括號來分組：推薦 (Grouping parentheses: recommended)</h2>
<hr>
<p>小括號是開發者及程式碼審查者有以下的認定才可省略，拿掉後不會有機會造成對程式碼的誤解，以及會讓程式碼變得易於閱讀。我們沒有理由去假設所有人都將 JAVA 運算子的優先表都記下來。</p>
<h2 id="s4.8_specific_constructs">4.8 特定結構 (Specific constructs)</h2>
<hr>
<p style="padding-left: 30px;"><strong>4.8.1 Enum classes</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.1-enum-classes" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.1_enum_classes">譯文</a>)</strong><br>
<strong>4.8.2&nbsp;Variable declarations</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.2-variable-declarations" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.2_variable_declarations">譯文</a>)</strong><br>
<strong>4.8.3&nbsp;Arrays</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.3-arrays" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.3_arrays">譯文</a>)</strong><br>
<strong>4.8.4&nbsp;Switch statements</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.4-switch" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.4_switch">譯文</a>)</strong><br>
<strong>4.8.5&nbsp;Annotations</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.5-annotations" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.5_annotations">譯文</a>)</strong><br>
<strong>4.8.6&nbsp;Comments</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.6-comments" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.6_comments">譯文</a>)</strong><br>
<strong>4.8.7&nbsp;Modifiers</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.7-modifiers" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.7_modifiers">譯文</a>)</strong><br>
<strong>4.8.8&nbsp;Numeric Literals</strong><strong>&nbsp;(<a href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.8-numeric-literals" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.8_numeric_literals">譯文</a>)</strong></p>
<h3 id="s4.8.1_enum_classes">4.8.1 列舉類別 (Enum classes)</h3>
<hr>
<p>每個逗號皆要跟著列舉的項目，也可斷行。</p>
<p>一個沒有函式以及說明文字在其中的列舉，可以將其格式寫成像陣列的初始化：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ee82422917006" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">private enum Suit { CLUBS, HEARTS, SPADES, DIAMONDS }</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ee82422917006-1">1</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ee82422917006-1"><span class="crayon-m">private</span><span class="crayon-h"> </span><span class="crayon-t">enum</span><span class="crayon-h"> </span><span class="crayon-e">Suit</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-h"> </span><span class="crayon-v">CLUBS</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-v">HEARTS</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-v">SPADES</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-i">DIAMONDS</span><span class="crayon-h"> </span><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0003 seconds] -->
<p>既然列舉類別是為類別，則其他格式的規則皆視同於類別。</p>
<h3 id="s4.8.2_variable_declarations">4.8.2 變數宣告 (Variable declarations)</h3>
<hr>
<p style="padding-left: 30px;"><strong>4.8.2.1 One variable per declaration (<a title="4.8.2.1 One variable per declaration" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.2.1-variables-per-declaration" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.2.1_variables_per_declaration">譯文</a>)<br>
</strong><strong>4.8.2.2 Declared when needed, initialized as soon as possible (<a title="4.8.2.2 Declared when needed, initialized as soon as possible" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.2.2-variables-limited-scope" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.2.2_variables_limited_scope">譯文</a>)</strong></p>
<h4 id="s4.8.2.1_variables_per_declaration">4.8.2.1 每個變數獨立宣告&nbsp;(One variable per declaration)</h4>
<hr>
<p>每個變數在宣告 (屬性 (field) 或是區域變數 (local)) 時，僅宣告一個變數，也就是說不要寫成
			<span id="crayon-5c933e329ee85973959485" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">int a, b;</span></span>&nbsp;。</p>
<h4 id="s4.8.2.2_variables_limited_scope">4.8.2.2&nbsp;需要時才宣告，並盡可能給予初始化&nbsp;(Declared when needed, initialized as soon as possible)</h4>
<hr>
<p>區域變數不要習慣在其區塊或是類區塊結構的一開始就全部宣告了。而是要在該區域變數第一次被使用時才宣告，盡量將他的範圍最小化。宣告區域變數時通常就會有初始值，或是在其宣告後立刻被初始化。</p>
<h3 id="s4.8.3_arrays">4.8.3 陣列 (Arrays)</h3>
<hr>
<p style="padding-left: 30px;"><strong>4.8.3.1 Array initializers: can be “block-like” (<a title="4.8.3.1 Array initializers: can be &quot;block-like&quot;" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.3.1-array-initializers" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.3.1_array_initializers">譯文</a>)<br>
</strong><strong>4.8.3.2 No C-style array declarations&nbsp;(<a title="4.8.3.2 No C-style array declarations" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.3.1-array-initializers" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.3.2_array_declarations">譯文</a>)</strong></p>
<h4 id="s4.8.3.1_array_initializers">4.8.3.1 陣列初始化：可為「類區塊結構」 (Array initializers: can be “block-like”)</h4>
<hr>
<p>所有陣列在初始化時，只要格式為「類區塊結構&nbsp;(block-like construct)」皆可。如下範例都可以 (並<strong>未</strong>全部列出)：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ee87478305250" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">new int[] {
  0, 1, 2, 3
}

new int[] {
  0, 1,
  2, 3
}      

new int[] {
  0,
  1,
  2,
  3,
}

new int[]
    {0, 1, 2, 3}</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-4">4</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-5">5</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-6">6</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-7">7</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-8">8</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-9">9</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-10">10</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-11">11</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-12">12</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-13">13</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-14">14</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-15">15</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-16">16</div><div class="crayon-num" data-line="crayon-5c933e329ee87478305250-17">17</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee87478305250-18">18</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ee87478305250-1"><span class="crayon-r">new</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-sy">[</span><span class="crayon-sy">]</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-2"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-cn">0</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">1</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">2</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">3</span></div><div class="crayon-line" id="crayon-5c933e329ee87478305250-3"><span class="crayon-sy">}</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-4">&nbsp;</div><div class="crayon-line" id="crayon-5c933e329ee87478305250-5"><span class="crayon-r">new</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-sy">[</span><span class="crayon-sy">]</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-6"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-cn">0</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">1</span><span class="crayon-sy">,</span></div><div class="crayon-line" id="crayon-5c933e329ee87478305250-7"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-cn">2</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">3</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-8"><span class="crayon-sy">}</span><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></div><div class="crayon-line" id="crayon-5c933e329ee87478305250-9">&nbsp;</div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-10"><span class="crayon-r">new</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-sy">[</span><span class="crayon-sy">]</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line" id="crayon-5c933e329ee87478305250-11"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-cn">0</span><span class="crayon-sy">,</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-12"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-cn">1</span><span class="crayon-sy">,</span></div><div class="crayon-line" id="crayon-5c933e329ee87478305250-13"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-cn">2</span><span class="crayon-sy">,</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-14"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-cn">3</span><span class="crayon-sy">,</span></div><div class="crayon-line" id="crayon-5c933e329ee87478305250-15"><span class="crayon-sy">}</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-16">&nbsp;</div><div class="crayon-line" id="crayon-5c933e329ee87478305250-17"><span class="crayon-r">new</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-sy">[</span><span class="crayon-sy">]</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee87478305250-18"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-sy">{</span><span class="crayon-cn">0</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">1</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">2</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-cn">3</span><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0009 seconds] -->
<p></p>
<h4 id="s4.8.3.2_array_declarations">4.8.3.2 宣告方式不要用&nbsp;C 語言風格&nbsp;(No C-style array declarations)</h4>
<hr>
<p>中括號是型別的一部份，而非變數的一部份：
			<span id="crayon-5c933e329ee89365345974" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">Srting[] args</span></span>&nbsp;而不是
			<span id="crayon-5c933e329ee8b553440058" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">String args[]</span></span>&nbsp;。</p>
<h3 id="s4.8.4_switch">4.8.4 Switch 敘述區塊 (Switch statements)</h3>
<hr>
<p style="padding-left: 30px;"><strong>4.8.4.1 Indentation (<a title="4.8.4.1 Indentation" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.4.1-switch-indentation" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.4.1_switch_indentation">譯文</a>)<br>
</strong><strong>4.8.4.2 Fall-through: commented&nbsp;(<a title="4.8.4.2 Fall-through: commented" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.4.2-switch-fall-through" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.4.2_switch_fall_through">譯文</a>)<br>
</strong><strong>4.8.4.3 The default case is present&nbsp;(<a title="4.8.4.3 The default case is present" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s4.8.4.3-switch-default" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.4.3_switch_default">譯文</a>)</strong></p>
<p><strong>術語說明：</strong>在 switch 敘述區塊的括號中，有一或多個敘述群組。每個敘述群組包含一個或多個 switch 標籤 (像是&nbsp;
			<span id="crayon-5c933e329ee8e786331944" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">case FOO:</span></span>&nbsp;或是&nbsp;
			<span id="crayon-5c933e329ee90752844140" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">default:</span></span>&nbsp;)，其後接續著一或多個程式敘述。</p>
<h4 id="s4.8.4.1_switch_indentation">4.8.4.1 縮排 (Indentation)</h4>
<hr>
<p>和其他區塊一樣， switch 區塊的內容縮排都是 +2 個空白。</p>
<p>在 switch 標籤後，每新的一行，若是該區塊的開始，就 +2 個空白做為縮排層級的增加。並在下一個標籤退回到上一個縮排層級，以表示上一個區塊已經結束。</p>
<h4 id="s4.8.4.2_switch_fall_through">4.8.4.2 Fall-through：註解 (Fall-through: commented)</h4>
<hr>
<p>在 switch 區塊中，每個敘述群組都會有個終止點 (像是 
			<span id="crayon-5c933e329ee92908470613" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">break</span></span>&nbsp;、
			<span id="crayon-5c933e329ee94770150894" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">continue</span></span>&nbsp;、
			<span id="crayon-5c933e329ee97210276522" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">return</span></span>&nbsp;或是拋出異常)，或是標上註釋以表將會或是可能繼續往下一個敘述群組執行。該註解只要足以表達出 fall-through 即可 (通常都是用
			<span id="crayon-5c933e329ee99154823030" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">// fall through</span></span>&nbsp;)。這特殊的註解不需要出現在 switch 區塊中的最後一個敘述。例如：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ee9b519334723" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">switch (input) {
  case 1:
  case 2:
    prepareOneOrTwo();
    // fall through
  case 3:
    handleOneTwoOrThree();
    break;
  default:
    handleLargeNumber(input);
}</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ee9b519334723-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee9b519334723-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ee9b519334723-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee9b519334723-4">4</div><div class="crayon-num" data-line="crayon-5c933e329ee9b519334723-5">5</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee9b519334723-6">6</div><div class="crayon-num" data-line="crayon-5c933e329ee9b519334723-7">7</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee9b519334723-8">8</div><div class="crayon-num" data-line="crayon-5c933e329ee9b519334723-9">9</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee9b519334723-10">10</div><div class="crayon-num" data-line="crayon-5c933e329ee9b519334723-11">11</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ee9b519334723-1"><span class="crayon-st">switch</span><span class="crayon-h"> </span><span class="crayon-sy">(</span><span class="crayon-v">input</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee9b519334723-2"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-st">case</span><span class="crayon-h"> </span><span class="crayon-cn">1</span><span class="crayon-o">:</span></div><div class="crayon-line" id="crayon-5c933e329ee9b519334723-3"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-st">case</span><span class="crayon-h"> </span><span class="crayon-cn">2</span><span class="crayon-o">:</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee9b519334723-4"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-e">prepareOneOrTwo</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329ee9b519334723-5"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-c">// fall through</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee9b519334723-6"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-st">case</span><span class="crayon-h"> </span><span class="crayon-cn">3</span><span class="crayon-o">:</span></div><div class="crayon-line" id="crayon-5c933e329ee9b519334723-7"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-e">handleOneTwoOrThree</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee9b519334723-8"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-st">break</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329ee9b519334723-9"><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-st">default</span><span class="crayon-o">:</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee9b519334723-10"><span class="crayon-h">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="crayon-e">handleLargeNumber</span><span class="crayon-sy">(</span><span class="crayon-v">input</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329ee9b519334723-11"><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0006 seconds] -->
<p></p>
<h4 id="s4.8.4.3_switch_default">4.8.4.3 default 一定要有 (The default case is present)</h4>
<hr>
<p>每個 switch 敘述的群組中都一定包含
			<span id="crayon-5c933e329ee9d770713711" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">default</span></span>&nbsp;，就算裡面沒有程式碼。</p>
<h3 id="s4.8.5_annotations">4.8.5 標注 (Annotations)</h3>
<hr>
<p>應用標注 (annotation) 的類別、函式與建構函式緊接於其文件區塊之後，每個標注皆自己獨立於一行(意即一行一個標注) 。而這幾行的斷行並不屬於換行 (4.5 Line-wrapping)，所以，縮排層級並不會增加。例如：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329ee9f933727962" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">@Override
@Nullable
public String getNameIfPresent() { ... }</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329ee9f933727962-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329ee9f933727962-2">2</div><div class="crayon-num" data-line="crayon-5c933e329ee9f933727962-3">3</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329ee9f933727962-1"><span class="crayon-n">@Override</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329ee9f933727962-2"><span class="crayon-n">@Nullable</span></div><div class="crayon-line" id="crayon-5c933e329ee9f933727962-3"><span class="crayon-m">public</span><span class="crayon-h"> </span><span class="crayon-t">String</span><span class="crayon-h"> </span><span class="crayon-e">getNameIfPresent</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-h"> </span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-h"> </span><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0003 seconds] -->
<p><strong>例外</strong>： 單個無參數的注解可以和其屬名的第一行放在一起。例如：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329eea2057092409" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">@Override public int hashCode() { ... }</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329eea2057092409-1">1</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329eea2057092409-1"><span class="crayon-n">@Override</span><span class="crayon-h"> </span><span class="crayon-m">public</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-h"> </span><span class="crayon-e">hashCode</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-h"> </span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-sy">.</span><span class="crayon-h"> </span><span class="crayon-sy">}</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0003 seconds] -->
<p>應用標注 (annotation) 的屬性 (filed) 緊接於其文件區塊之後，但在這個狀況下，若是有多個標注可以將其呈列於同一行。例如：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329eea4322200002" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">@Partial @Mock DataLoader loader;</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329eea4322200002-1">1</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329eea4322200002-1"><span class="crayon-n">@Partial</span><span class="crayon-h"> </span><span class="crayon-n">@Mock</span><span class="crayon-h"> </span><span class="crayon-e">DataLoader </span><span class="crayon-v">loader</span><span class="crayon-sy">;</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0002 seconds] -->
<p>參數與區域變數的標注沒有特殊的規則與格式。</p>
<h3 id="s4.8.6_comments">4.8.6 註解 (Comments)</h3>
<hr>
<p style="padding-left: 30px;"><strong>4.8.6.1 Block comment style (<a title="4.8.6.1 Block comment style" href="http://google-styleguide.googlecode.com/svn/trunk/javaguidelink.png" target="_blank" class="fancybox image">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s4.8.6.1_block_comment_style">譯文</a>)</strong></p>
<h4 id="s4.8.6.1_block_comment_style">4.8.6.1 註解區塊的風格 (Block comment style)</h4>
<hr>
<p>註解區塊的縮排，和其接連的程式碼同一層級。可用
			<span id="crayon-5c933e329eea6771839024" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">/* ... */</span></span>&nbsp;或
			<span id="crayon-5c933e329eea8595255409" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">// ...</span></span>&nbsp;。若是這種註解風格&nbsp;
			<span id="crayon-5c933e329eeaa720821523" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">/* ... */</span></span>&nbsp;有多行時，其子行的起始必需有 
			<span id="crayon-5c933e329eeac644766172" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">*</span></span>，而該星號需對齊上一行的
			<span id="crayon-5c933e329eeae778014970" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">*</span></span>&nbsp;。</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329eeb0375295975" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code" style="display: none;"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">/*
 * This is          // And so           /* Or you can
 * okay.            // is this.          * even do this. */
 */</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329eeb0375295975-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eeb0375295975-2">2</div><div class="crayon-num" data-line="crayon-5c933e329eeb0375295975-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eeb0375295975-4">4</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329eeb0375295975-1"><span class="crayon-c">/*</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eeb0375295975-2"><span class="crayon-c"> * This is&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// And so&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; /* Or you can</span></div><div class="crayon-line" id="crayon-5c933e329eeb0375295975-3"><span class="crayon-c"> * okay.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// is this.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* even do this. */</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eeb0375295975-4"><span class="crayon-h"> </span><span class="crayon-o">*</span><span class="crayon-o">/</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0001 seconds] -->
<p>註解不要將之放在由星號或是其他符號繪製的方框中。</p>
<p>[box style=”light-yellow info&nbsp;rounded” ] <strong>Tip:</strong><br>
當在撰寫多行的程式碼時，想要在換行時用程式碼自動格式化的話，就採用 
			<span id="crayon-5c933e329eeb3165459828" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">/* ... */</span></span>&nbsp;風格，因為大部份格式化換行的自動格式化，並沒有支援 
			<span id="crayon-5c933e329eeb5292987344" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">// ...</span></span>&nbsp;這種風格。[/box]</p>
<h3 id="s4.8.7_modifiers">4.8.7 修飾詞 (Modifiers)</h3>
<hr>
<p>類別與成員的修飾詞，其呈現順序就依 Java 語言的規範：</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329eeb7906941124" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">public protected private abstract static final transient volatile synchronized native strictfp</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329eeb7906941124-1">1</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329eeb7906941124-1"><span class="crayon-m">public</span><span class="crayon-h"> </span><span class="crayon-m">protected</span><span class="crayon-h"> </span><span class="crayon-m">private</span><span class="crayon-h"> </span><span class="crayon-m">abstract</span><span class="crayon-h"> </span><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-m">transient</span><span class="crayon-h"> </span><span class="crayon-m">volatile</span><span class="crayon-h"> </span><span class="crayon-m">synchronized</span><span class="crayon-h"> </span><span class="crayon-m">native</span><span class="crayon-h"> </span><span class="crayon-m">strictfp</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0003 seconds] -->
<p></p>
<h3 id="s4.8.8_numeric_literals">4.8.8 數值行型 (Numeric Literals)</h3>
<hr>
<p>
			<span id="crayon-5c933e329eeb9155023516" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">long</span></span>&nbsp;於整數型別後加上後綴大寫字母&nbsp;
			<span id="crayon-5c933e329eebb968506294" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">L</span></span>，千萬別用小寫字母 (避免和數字 
			<span id="crayon-5c933e329eebd724205148" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">1</span></span>&nbsp;)。例如，
			<span id="crayon-5c933e329eebf956589531" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">3000000000L</span></span>&nbsp;而不要寫成&nbsp;
			<span id="crayon-5c933e329eec1345271985" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">30000000000l</span></span>。</p>