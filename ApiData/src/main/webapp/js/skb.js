var _skb = (function() {
	var l, c, n = "_softkb", p = "_skbtid", b = "_k", a = "k_ok", k = "k_cls", o = "k_bac", i = "k_cap", e = [
			[ "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" ],
			[ "q", "w", "e", "r", "t", "y", "u", "i", "o", "p" ],
			[ "a", "s", "d", "f", "g", "h", "j", "k", "l" ],
			[ "z", "x", "c", "v", "b", "n", "m" ] ], h = 306;
	function g(t) {
		var q = navigator.userAgent.toLowerCase(), r = (q.indexOf("opera") != -1), u = (q
				.indexOf("msie") != -1 && !r);
		if (t.parentNode === null || t.style.display == "none") {
			return false
		}
		var D = null, C, B, z;
		if (t.getBoundingClientRect) {
			z = t.getBoundingClientRect();
			var s = Math.max(document.documentElement.scrollTop,
					document.body.scrollTop);
			var v = Math.max(document.documentElement.scrollLeft,
					document.body.scrollLeft);
			return {
				x : Math.floor(z.left + v),
				y : Math.floor(z.top + s)
			}
		} else {
			if (document.getBoxObjectFor) {
				z = document.getBoxObjectFor(t);
				var A = (t.style.borderLeftWidth) ? parseInt(t.style.borderLeftWidth)
						: 0;
				var w = (t.style.borderTopWidth) ? parseInt(t.style.borderTopWidth)
						: 0;
				C = z.x - A;
				B = z.y - w
			} else {
				C = t.offsetLeft;
				B = t.offsetTop;
				D = t.offsetParent;
				if (D != t) {
					while (D) {
						C += D.offsetLeft;
						B += D.offsetTop;
						D = D.offsetParent
					}
				}
				if (q.indexOf("opera") != -1
						|| (q.indexOf("safari") != -1 && t.style.position == "absolute")) {
					C -= document.body.offsetLeft;
					B -= document.body.offsetTop
				}
			}
		}
		if (t.parentNode) {
			D = t.parentNode
		} else {
			D = null
		}
		while (D && D.tagName != "BODY" && D.tagName != "HTML") {
			C -= D.scrollLeft;
			B -= D.scrollTop;
			if (D.parentNode) {
				D = D.parentNode
			} else {
				D = null
			}
		}
		return {
			x : Math.floor(C),
			y : Math.floor(B)
		}
	}
	function j() {
		if (c) {
			var q = c.maxLength;
			if (q && c.value.length >= q) {
				//alert("您输入的字符长度不能大于" + q);
				return
			}
			c.value = c.value + this.innerHTML
		}
	}
	function d() {
		if (document.getElementById(n)) {
			return
		}
		var y = [], w, v, s, q, z;
		l = document.createElement("div");
		l.setAttribute("id", n);
		l.className = "skb";
		l.style.display = "none";
		y.push('<table id="');
		y.push(p);
		y
				.push('" cellspacing="2" cellpadding="0" bordercolor="#ffffff" border="0" bgcolor="#ffffff" align="center" class="skbbody"><tbody>');
		y
				.push('<tr><td height="16px" valign="bottom" colspan=11 class="skbhead">为了您的信息安全请使用软键盘</td></tr>');
		for ( var w = 0; w < e.length; w++) {
			var u = e[w];
			y.push('<tr style="width:');
			y.push(h);
			y.push('px;">');
			for (s = 0; s < u.length; s++) {
				q = parseInt(Math.random() * u.length);
				z = u[s];
				u[s] = u[q];
				u[q] = z
			}
			for ( var v = 0; v < u.length; v++) {
				y
						.push('<td width="36px" height="30px" class="skbbout" style="cursor:pointer;">');
				y.push(u[v]);
				y.push("</td>")
			}
			if (w == 0) {
				y.push('<td id="');
				y.push(i);
				y
						.push('" width="26px" height="20px" rowspan=2 class="skbbout" style="cursor:pointer;">大<br>写</td>')
			} else {
				if (w == 2) {
					y.push('<td id="');
					y.push(k);
					y
							.push('" width="52px" height="20px" colspan=2 class="skbbout" style="cursor:pointer;">清空</td>')
				} else {
					if (w == 3) {
						y.push('<td id="');
						y.push(o);
						y
								.push('" width="52px" height="20px" colspan=2 class="skbbout" style="cursor:pointer;">回退</td>');
						y.push('<td id="');
						y.push(a);
						y
								.push('" width="52px" height="20px" colspan=2 class="skbbout" style="cursor:pointer;">确定</td>')
					}
				}
			}
			y.push("</tr>")
		}
		y.push("</tbody></table>");
		l.innerHTML = y.join("");
		document.body.appendChild(l);
		var x = document.getElementById(p);
		x.rows[0].cells[0].onselectstart = function() {
			return false
		};
		for (w = 1; w < x.rows.length; w++) {
			for (v = 0; v < x.rows[w].cells.length; v++) {
				var t = x.rows[w].cells[v];
				t.onselectstart = function() {
					return false
				};
				t.onmouseover = function() {
					this.className = "skbbover"
				};
				t.onmouseout = function() {
					this.className = "skbbout"
				};
				if (t.id == k) {
					t.onclick = function() {
						if (c) {
							c.value = "";
						}
					}
				} else {
					if (t.id == a) {
						t.onclick = function() {
							m()
						}
					} else {
						if (t.id == o) {
							t.onclick = function() {
								c.value = c.value.substr(0, c.value.length - 1)
							}
						} else {
							if (t.id == i) {
								t.onclick = function() {
									this._u = this._u ? (this._u == 1 ? 2 : 1)
											: 2;
									this.innerHTML = this._u == 2 ? '小<br>写'
											: '大<br>写';
									var r = document.getElementsByTagName("td");
									for ( var A = 0; A < r.length; A++) {
										if (r[A].className == "skbbout") {
											r[A].innerHTML = r[A].innerHTML[this._u == 2 ? "toUpperCase"
													: "toLowerCase"]()
										}
									}
								}
							} else {
								t.onmouseup = j
							}
						}
					}
				}
			}
		}
	}
	function f(q) {
		d();
		if (!l || !q) {
			alert("软键盘初始化失败");
			return
		}
		if (l.style.display == "none") {
			l.style.display = ""
		}
		var s = g(q);
		var r = Math.max(q.offsetHeight, q.clientHeight) || 0;
		l.style.left = s.x;
		l.style.top = s.y + r;
		c = q
	}
	function m() {
		if (l) {
			l.style.display = "none";
		}
		//处理光标位置，置于输入框最右边
		var s = c.value;
		c.focus();
		c.value="";
		c.value=s;
	}
	
	return (function() {
		return {
			showSoftKB : f,
			hideSoftKB : m
		}
	})()
})();