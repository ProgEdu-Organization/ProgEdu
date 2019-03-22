<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<h1 id="s5_Naming">5. 命名(Naming)</h1>
<hr>
<p style="padding-left: 30px;"><strong>5.1 Rules common to all identifiers (<a title="5.1 Rules common to all identifiers" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.1-identifier-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.1_identifier_names">譯文</a>)</strong><br>
<strong>5.2 Rules by identifier type&nbsp;(<a title="5.2 Rules by identifier type " href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2-specific-identifier-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2_specific_identifier_names">譯文</a>)</strong><br>
<strong>5.3 Camel case: defined&nbsp;(<a title="5.3 Camel case: defined " href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.3-camel-case" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.3_camel_case">譯文</a>)</strong></p>
<h2 id="s5.1_identifier_names">5.1 識別字通則 (Rules common to all identifiers)</h2>
<hr>
<p>識別字僅能使用英文字母與數字，若是需要分成兩個部份以上，請使用下底線(_)。因此，每個符號皆需符合正規表示式
			<span id="crayon-5c933e329eec3311190155" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">w+</span></span>&nbsp;。</p>
<p>在 Google style 中會有一些特別的前綴或後綴用法，舉例像是
			<span id="crayon-5c933e329eec6667311686" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">name_</span></span>、
			<span id="crayon-5c933e329eec8614444514" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">mName</span></span>、
			<span id="crayon-5c933e329eeca617426110" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">s_name</span></span>以及
			<span id="crayon-5c933e329eecc492386083" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">kName</span></span>，在這邊都不使用。</p>
<h2 id="s5.2_specific_identifier_names">5.2 各識別類型的規則 (Rules by identifier type)</h2>
<hr>
<p style="padding-left: 30px;"><strong>5.2.1 Package names</strong><strong>&nbsp;(<a title="5.2.1 Package names " href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.1-package-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.1_package_names">譯文</a>)</strong><br>
<strong>5.2.2&nbsp;Class names</strong><strong>&nbsp;(<a title="5.2.2 Class names " href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.2-class-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.2_class_names">譯文</a>)</strong><br>
<strong>5.2.3&nbsp;Method names</strong><strong>&nbsp;(<a title="5.2.3 Method names " href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.3-method-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.3_method_names">譯文</a>)</strong><br>
<strong>5.2.4&nbsp;Constant names</strong><strong>&nbsp;(<a title="5.2.4 Constant names " href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.4-constant-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.4_constant_names">譯文</a>)</strong><br>
<strong>5.2.5&nbsp;Non-constant field names</strong><strong>&nbsp;(<a title="5.2.5 Non-constant field names" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.5-non-constant-field-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.5_non_constant_field_names">譯文</a>)</strong><br>
<strong>5.2.6&nbsp;Parameter names</strong><strong>&nbsp;(<a title="5.2.6 Parameter names" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.6-parameter-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.6_parameter_names">譯文</a>)</strong><br>
<strong>5.2.7&nbsp;Local variable names</strong><strong>&nbsp;(<a title="5.2.7 Local variable names" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.7-local-variable-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.7_local_variable_names">譯文</a>)</strong><br>
<strong>5.2.8&nbsp;Type variable names</strong><strong>&nbsp;(<a title="5.2.8 Type variable names" href="http://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.8-type-variable-names" target="_blank">原文</a>、<a href="http://blog.mosil.biz/2014/05/java-style-guide/#s5.2.8_type_variable_names">譯文</a>)</strong></p>
<h3 id="s5.2.1_package_names">5.2.1 Package 名命 (Package names)</h3>
<hr>
<p>Package 名稱全部小寫，連續單字直接寫在一起(不用下底線(_))。例如，
			<span id="crayon-5c933e329eece755465620" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">com.example.deepspace</span></span>&nbsp;，不要這樣
			<span id="crayon-5c933e329eed0800741638" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">com.example.deepSpace</span></span>&nbsp;或
			<span id="crayon-5c933e329eed2184379345" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">com.example.deep_space</span></span>&nbsp;。</p>
<h3 id="s5.2.2_class_names">5.2.2 類別命名 (Class names)</h3>
<hr>
<p>類別名稱採用大寫開始的駝峰命名法(UpperCamelCase)。</p>
<p>類別名稱為名詞或是名詞片語。例如，
			<span id="crayon-5c933e329eed4998357458" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">Character</span></span>&nbsp;或&nbsp;
			<span id="crayon-5c933e329eed7493787413" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">ImmutableList</span></span>，介面(interface)也使用名詞或是名詞片語(如：
			<span id="crayon-5c933e329eed9260430872" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">List</span></span>)，但有時候會用形容詞或是形容詞片語取而代之(如：
			<span id="crayon-5c933e329eedb769156803" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">Readable</span></span>)。</p>
<h3 id="s5.2.3_method_names">5.2.3 函式命名 (Method names)</h3>
<hr>
<p>函式名稱採用小寫開始的駝峰命名法(lowerCamelCase)。</p>
<p>函式名稱通常都是動詞或是動詞片語。例如，
			<span id="crayon-5c933e329eedd725719128" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">sendMessage</span></span>&nbsp;或是
			<span id="crayon-5c933e329eedf821322066" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">stop</span></span>&nbsp;。</p>
<p>下底線可以做為 JUnit 的測試函式名稱與邏輯元件的分隔。一個典型的模式
			<span id="crayon-5c933e329eee1762235732" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">test&lt;MethodUnderTest&gt;_&lt;state&gt;</span></span>&nbsp;，範例：
			<span id="crayon-5c933e329eee3039400853" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">testPop_emptyStack</span></span>&nbsp;。這邊並沒有唯一的正確方式去命名測試函式。</p>
<h3 id="s5.2.4_constant_names">5.2.4 常數命名 (Constant names)</h3>
<hr>
<p>常數名稱採用
			<span id="crayon-5c933e329eee5224774540" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">CONSTANT_CASE</span></span>&nbsp;，全部採用大寫字母，使用下底線分隔。但究竟什麼是常數呢？</p>
<p>所以有常數的屬性 (Field) 皆是 static final， 但並非所有 static final 屬性的變數皆為常數。在確定變數是為常數前，需要先考慮他是否真的像一個常數。舉例來說，當所有在實作的觀察階段會改變時，那這個變數幾乎就可以肯定不是一個常數。而通常只是打算永不改變狀態是不夠的。</p>
<p>範例</p><!-- Crayon Syntax Highlighter v_2.7.2_beta -->

		<div id="crayon-5c933e329eee8890210997" class="crayon-syntax crayon-theme-github crayon-font-monaco crayon-os-pc print-yes notranslate" data-settings=" minimize scroll-mouseover" style="margin: 12px auto; float: none; clear: none; font-size: 12px !important; line-height: 15px !important; height: auto;">
		
			<div class="crayon-toolbar" data-settings=" show" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><span class="crayon-title"></span>
			<div class="crayon-tools" style="font-size: 12px !important;height: 18px !important; line-height: 18px !important;"><div class="crayon-button crayon-nums-button crayon-pressed" title="Toggle Line Numbers"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-plain-button" title="Toggle Plain Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-wrap-button" title="Toggle Line Wrap"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-expand-button" title="Expand Code"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-copy-button" title="Copy"><div class="crayon-button-icon"></div></div><div class="crayon-button crayon-popup-button" title="Open Code In New Window"><div class="crayon-button-icon"></div></div></div></div>
			<div class="crayon-info" style="min-height: 16.8px !important; line-height: 16.8px !important;"></div>
			<div class="crayon-plain-wrap"><textarea wrap="soft" class="crayon-plain print-no" data-settings="dblclick" readonly="" style="tab-size: 4; font-size: 12px !important; line-height: 15px !important; z-index: 0; opacity: 0; overflow: hidden;">// 常數
static final int NUMBER = 5;
static final ImmutableList&lt;String&gt; NAMES = ImmutableList.of("Ed", "Ann");
static final Joiner COMMA_JOINER = Joiner.on(',');  // because Joiner is immutable
static final SomeMutableType[] EMPTY_ARRAY = {};
enum SomeEnum { ENUM_CONSTANT }

// 非常數
static String nonFinal = "non-final";
final String nonStatic = "non-static";
static final Set&lt;String&gt; mutableCollection = new HashSet&lt;String&gt;();
static final ImmutableSet&lt;SomeMutableType&gt; mutableElements = ImmutableSet.of(mutable);
static final Logger logger = Logger.getLogger(MyClass.getName());
static final String[] nonEmptyArray = {"these", "can", "change"};</textarea></div>
			<div class="crayon-main" style="position: relative; z-index: 1; overflow: hidden;">
				<table class="crayon-table" style="">
					<tbody><tr class="crayon-row">
				<td class="crayon-nums " data-settings="show">
					<div class="crayon-nums-content" style="font-size: 12px !important; line-height: 15px !important;"><div class="crayon-num" data-line="crayon-5c933e329eee8890210997-1">1</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eee8890210997-2">2</div><div class="crayon-num" data-line="crayon-5c933e329eee8890210997-3">3</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eee8890210997-4">4</div><div class="crayon-num" data-line="crayon-5c933e329eee8890210997-5">5</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eee8890210997-6">6</div><div class="crayon-num" data-line="crayon-5c933e329eee8890210997-7">7</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eee8890210997-8">8</div><div class="crayon-num" data-line="crayon-5c933e329eee8890210997-9">9</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eee8890210997-10">10</div><div class="crayon-num" data-line="crayon-5c933e329eee8890210997-11">11</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eee8890210997-12">12</div><div class="crayon-num" data-line="crayon-5c933e329eee8890210997-13">13</div><div class="crayon-num crayon-striped-num" data-line="crayon-5c933e329eee8890210997-14">14</div></div>
				</td>
						<td class="crayon-code"><div class="crayon-pre" style="font-size: 12px !important; line-height: 15px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;"><div class="crayon-line" id="crayon-5c933e329eee8890210997-1"><span class="crayon-c">// 常數</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eee8890210997-2"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-t">int</span><span class="crayon-h"> </span><span class="crayon-v">NUMBER</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-cn">5</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329eee8890210997-3"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-v">ImmutableList</span><span class="crayon-e ">&lt;String&gt;</span><span class="crayon-h"> </span><span class="crayon-v">NAMES</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-v">ImmutableList</span><span class="crayon-sy">.</span><span class="crayon-e">of</span><span class="crayon-sy">(</span><span class="crayon-s">"Ed"</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-s">"Ann"</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eee8890210997-4"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-e">Joiner </span><span class="crayon-v">COMMA_JOINER</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-v">Joiner</span><span class="crayon-sy">.</span><span class="crayon-e">on</span><span class="crayon-sy">(</span><span class="crayon-s">','</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span><span class="crayon-h">&nbsp;&nbsp;</span><span class="crayon-c">// because Joiner is immutable</span></div><div class="crayon-line" id="crayon-5c933e329eee8890210997-5"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-v">SomeMutableType</span><span class="crayon-sy">[</span><span class="crayon-sy">]</span><span class="crayon-h"> </span><span class="crayon-v">EMPTY_ARRAY</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-sy">}</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eee8890210997-6"><span class="crayon-t">enum</span><span class="crayon-h"> </span><span class="crayon-e">SomeEnum</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-h"> </span><span class="crayon-v">ENUM</span><span class="crayon-sy">_</span>CONSTANT<span class="crayon-h"> </span><span class="crayon-sy">}</span></div><div class="crayon-line" id="crayon-5c933e329eee8890210997-7">&nbsp;</div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eee8890210997-8"><span class="crayon-c">// 非常數</span></div><div class="crayon-line" id="crayon-5c933e329eee8890210997-9"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-t">String</span><span class="crayon-h"> </span><span class="crayon-v">nonFinal</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-s">"non-final"</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eee8890210997-10"><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-t">String</span><span class="crayon-h"> </span><span class="crayon-v">nonStatic</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-s">"non-static"</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329eee8890210997-11"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-v">Set</span><span class="crayon-e ">&lt;String&gt;</span><span class="crayon-h"> </span><span class="crayon-v">mutableCollection</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-r">new</span><span class="crayon-h"> </span><span class="crayon-v">HashSet</span><span class="crayon-e ">&lt;String&gt;</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eee8890210997-12"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-v">ImmutableSet</span><span class="crayon-e ">&lt;SomeMutableType&gt;</span><span class="crayon-h"> </span><span class="crayon-v">mutableElements</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-v">ImmutableSet</span><span class="crayon-sy">.</span><span class="crayon-e">of</span><span class="crayon-sy">(</span><span class="crayon-v">mutable</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line" id="crayon-5c933e329eee8890210997-13"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-e">Logger </span><span class="crayon-v">logger</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-v">Logger</span><span class="crayon-sy">.</span><span class="crayon-e">getLogger</span><span class="crayon-sy">(</span><span class="crayon-v">MyClass</span><span class="crayon-sy">.</span><span class="crayon-e">getName</span><span class="crayon-sy">(</span><span class="crayon-sy">)</span><span class="crayon-sy">)</span><span class="crayon-sy">;</span></div><div class="crayon-line crayon-striped-line" id="crayon-5c933e329eee8890210997-14"><span class="crayon-m">static</span><span class="crayon-h"> </span><span class="crayon-m">final</span><span class="crayon-h"> </span><span class="crayon-t">String</span><span class="crayon-sy">[</span><span class="crayon-sy">]</span><span class="crayon-h"> </span><span class="crayon-v">nonEmptyArray</span><span class="crayon-h"> </span><span class="crayon-o">=</span><span class="crayon-h"> </span><span class="crayon-sy">{</span><span class="crayon-s">"these"</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-s">"can"</span><span class="crayon-sy">,</span><span class="crayon-h"> </span><span class="crayon-s">"change"</span><span class="crayon-sy">}</span><span class="crayon-sy">;</span></div></div></td>
					</tr>
				</tbody></table>
			</div>
		</div>
<!-- [Format Time: 0.0019 seconds] -->
<p>名稱通常為名詞或是名詞片語。</p>
<h3 id="s5.2.5_non_constant_field_names">5.2.5 非常數屬性命名 (Non-constant field names)</h3>
<hr>
<p>非常數屬性的名稱 (static 或是 其他)採用小寫開頭的駝峰命名法 (lowerCamelCase)&nbsp;。</p>
<p>名稱通常為名詞或是名詞片語。</p>
<h3 id="s5.2.6_parameter_names">5.2.6 參數命名&nbsp;(Parameter names)</h3>
<hr>
<p>參數名稱使用採用小寫開頭的駝峰命名法 (lowerCamelCase)&nbsp;。</p>
<p>要避免只有一個字符的名稱。</p>
<h3 id="s5.2.7_local_variable_names">5.2.7 區域變數命名 (Local variable names)</h3>
<hr>
<p>區域變數名稱採用採用小寫開頭的駝峰命名法 (lowerCamelCase)&nbsp;。可以使用縮寫這種較為其他名稱寬鬆的命名方式。</p>
<p>但仍要避免單字符的名稱，除了泛型與迴圈變數。</p>
<p>即便是 final 或是不可變的，區域變數是不可為常數的，當然，也不該使用常數變數的風格。</p>
<h3 id="s5.2.8_type_variable_names">5.2.8 型別變數命名&nbsp;(Type variable names)</h3>
<hr>
<p>每個型別的命名方式可在下面二法中擇一：</p>
<ul>
<li>一個大寫單字，其後用數字可接續(諸如：
			<span id="crayon-5c933e329eeea076056945" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">E</span></span>&nbsp;、
			<span id="crayon-5c933e329eeec544406216" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">T</span></span>&nbsp;、
			<span id="crayon-5c933e329eeee923191381" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">X</span></span>&nbsp;、
			<span id="crayon-5c933e329eef0683764059" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">T2</span></span>&nbsp;)。</li>
<li>可採用類別命名的方式(請見 5.2.2)，其後再接一個大寫字母
			<span id="crayon-5c933e329eef2436377500" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">T</span></span>&nbsp;。例如：
			<span id="crayon-5c933e329eef5814565758" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">RequestT</span></span>&nbsp;、
			<span id="crayon-5c933e329eef7194177195" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">FooBarT</span></span>&nbsp;。</li>
</ul>
<h2 id="s5.3_camel_case">5.3 &nbsp;駝峰命名定義 (Camel case: defined)</h2>
<hr>
<p>英文詞彙有時並非只有一種合理的駝峰命名表示方式，也有像「IPv6」或是「iOS」這樣的縮寫或是不尋常的表示法。為了改善使其規律，Google Style 將使用下方(幾乎)確定的方法。</p>
<p>以散文格式 (prose form) 為名稱的開頭：</p>
<ol>
<li>字詞皆改為 ASCII 碼並移除所有單引號，例如，「Müller’s algorithm」可以改變為「Muellers algorithm」。</li>
<li>上述步驟的結果，再依其中的空白以及其餘的符號(通為會連字符號)做為拆分點，拆成逐一的單字。
<ul>
<li>建議：若所有字都已經有其慣用的駝峰命名用法，仍是將其拆開(例：「AdWords」變成「ad words」。注意，像「iOS」這個並不是一個駝峰命名的形式，這個建議就不適用於這樣的例子。</li>
</ul>
</li>
<li>現在，將每個字母全部變成小寫(包含縮寫)，接著，只要將第一個字母改為大寫：
<ul>
<li>… 每個單字都改，為大寫開頭的駝峰&nbsp;(upper camel case)&nbsp;命名。</li>
<li>… 每個單字除了第一個單字不改，則為小寫開頭的駝峰 (lower camel case) 命名。</li>
</ul>
</li>
<li>最後，將所有單字連成一個識別符。</li>
</ol>
<p>需要注意的是，這邊的大小寫幾乎是已經無視原來的單字。範例：</p>
<ul>
<li>“XML HTTP request”
<ul>
<li>正確：<span style="color: #993300;">XmlHttpRequest</span></li>
<li>錯誤：<span style="color: #ff00ff;">XMLHTTPRequest</span></li>
</ul>
</li>
</ul>
<ul>
<li>“new customer ID”
<ul>
<li>正確：<span style="color: #993300;">newCustomerId</span></li>
<li>錯誤：<span style="color: #ff00ff;">newCustomerID</span></li>
</ul>
</li>
</ul>
<ul>
<li>“inner stopwatch”
<ul>
<li>正確：<span style="color: #993300;">innerStopwatch</span></li>
<li>錯誤：<span style="color: #ff00ff;">innerStopWatch</span></li>
</ul>
</li>
</ul>
<ul>
<li>“supports IPv6 on iOS?”
<ul>
<li>正確：<span style="color: #993300;">supportsIpv6OnIos</span></li>
<li>錯誤：<span style="color: #ff00ff;">supportsIPv6OnIOS</span></li>
</ul>
</li>
</ul>
<ul>
<li>“YouTube importer”
<ul>
<li>正確：<span style="color: #993300;">YouTubeImporter</span> 或 <span style="color: #993300;">YoutubeImporter</span>*</li>
<li>錯誤：</li>
</ul>
</li>
</ul>
<p>*允許， 但不建議。</p>
<p>[box&nbsp;&nbsp;style=”blue info&nbsp;rounded” ]<strong>Note:<br>
</strong>有些字在英語中，有無帶著連字符號都沒有錯，舉例來說「nonempty」以及「non-empty」二者皆對，所以方法若是命名成&nbsp;
			<span id="crayon-5c933e329eef9612770179" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">checkNonempty</span></span>&nbsp;以及
			<span id="crayon-5c933e329eefb261995379" class="crayon-syntax crayon-syntax-inline  crayon-theme-github crayon-theme-github-inline crayon-font-monaco" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important;"><span class="crayon-pre crayon-code" style="font-size: 12px !important; line-height: 15px !important;font-size: 12px !important; -moz-tab-size:4; -o-tab-size:4; -webkit-tab-size:4; tab-size:4;">checkNonEmpty</span></span>&nbsp;都是正確的。[/box]</p>