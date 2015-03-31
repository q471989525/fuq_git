/**
 * 变更合同想但信息添加 ------------zTree例子
 * @Document addAlterBoq
 * @date 2013-7-1
 * @Author Fuq
 */
var projCode_ = "";
var curPage = 1;
var pageSize = 50;//配置树每页显示多少条
var addAlterBoq = {
    addDiyDom: function(treeId, treeNode) {

        if (treeNode.size == -1 || treeNode.size <= 50) {
            return;
        }
        treeNode.page = 1;
        var aObj = $("#" + treeNode.tId + "_a");
        if ($("#addBtn_" + treeNode.id).length > 0)
            return;
        treeNode.maxPage = treeNode.size % pageSize == 0 ? treeNode.size / pageSize : Math.floor(treeNode.size / pageSize) + 1;
        var addStr = "<span class='button lastPage' id='lastBtn_" + treeNode.id
                + "' title='末页' onfocus='this.blur();'></span><span class='button nextPage' id='nextBtn_" + treeNode.id
                + "' title='下一页' onfocus='this.blur();'></span><span class='button prevPage' id='prevBtn_" + treeNode.id
                + "' title='上一页' onfocus='this.blur();'></span><span class='button firstPage' id='firstBtn_" + treeNode.id
                + "' title='首页' onfocus='this.blur();'></span><span>" + "第<span id='n_page_"
                + treeNode.id + "'>" + treeNode.page + "</span>页,共" + treeNode.maxPage + "页</span>";
        aObj.after(addStr);
        var first = $("#firstBtn_" + treeNode.id);
        var prev = $("#prevBtn_" + treeNode.id);
        var next = $("#nextBtn_" + treeNode.id);
        var last = $("#lastBtn_" + treeNode.id);
        first.bind("click", function() {
            if (!treeNode.isAjaxing) {
                addAlterBoq.goPage(treeNode, 1);
            }
        });
        last.bind("click", function() {
            if (!treeNode.isAjaxing) {

                addAlterBoq.goPage(treeNode, treeNode.maxPage);
            }
        });
        prev.bind("click", function() {
            if (!treeNode.isAjaxing) {

                addAlterBoq.goPage(treeNode, treeNode.page - 1);
            }
        });
        next.bind("click", function() {
            if (!treeNode.isAjaxing) {

                addAlterBoq.goPage(treeNode, treeNode.page + 1);
            }
        });
    },
    goPage: function(treeNode, page) {
        treeNode.page = page;
        if (treeNode.page < 1)
            treeNode.page = 1;
        if (treeNode.page > treeNode.maxPage)
            treeNode.page = treeNode.maxPage;
        curPage = treeNode.page;
        var zTree = $.fn.zTree.getZTreeObj("tree");
        $("#n_page_" + treeNode.id).text(treeNode.page);
        zTree.reAsyncChildNodes(treeNode, "refresh");
    },
    init: function(contId, projCode, masterId, show, menu_) {
        projCode_ = projCode;

        $("#BoqToolbar").ligerToolBar({items: eval(menu_)});
        $.fn.zTree.init($("#tree"), {
            check: {
                enable: true,
                autoCheckTrigger: true,
                nocheckInherit: true
            },
            async: {
                enable: true,
                // url: '../cont/contInfo/contBoqXM_treeList.action?contBoq.cont_id=' + contId + '&contBoq.proj_code=' + projCode + '&r=' + Math.random(),
                url: '../cont/contInfo/contBoqXM_findContBoqByPid.action?t=' + Math.random(),
                autoParam: ["boq_id", "page"],
                otherParam: {"contBoq.proj_code": projCode, "contBoq.cont_id": contId, "pagesize": pageSize},
                dataFilter: function(treeId, parentNode, childNodes) {
                    for (var i = 0; i < childNodes.length; i++) {
                        var codename = "";
                        if (childNodes[i].pay_item_flag != 1) {
                            childNodes[i].isParent = true;
                            //childNodes[i].open = false;
                        }
                        //alert(childNodes[i].chapter_code);
                        if (childNodes[i].chapter_code == '400')
                        {
                            if (childNodes[i].site != null)
                            {
                                codename += "[" + childNodes[i].site + "]" + " ";
                            }

                        } else {
                            if (childNodes[i].start_stack != null && childNodes[i].end_stack != null)
                            {
                                codename += "[" + childNodes[i].start_stack + "~" + childNodes[i].end_stack + "]" + " ";
                            }
                            if (childNodes[i].start_stack != null && childNodes[i].end_stack == null)
                            {
                                codename += "[" + childNodes[i].start_stack + "]" + " ";
                            }
                        }
                        codename = "<span style='color:#996600;'>" + codename + "</span>";
                        var cname = "";
                        //颜色说明：新增-绿，变更-红，桩号：
                        if (childNodes[i].alter_flag == 4) {
                            cname = "<span style='color:green;'>" + childNodes[i].code_name + "</span>";
                        } else if (childNodes[i].alter_flag == 0 || childNodes[i].alter_flag == null) {
                            cname = "" + childNodes[i].code_name + "";
                        } else {
                            cname = "<span style='color:rgb(255,0,0);'>" + childNodes[i].code_name + "</span>";
                        }
                        codename += cname;

                        childNodes[i].code_name = codename;

                    }

                    return childNodes;
                }
            },
            data: {
                key: {
                    children: "children",
                    name: "code_name",
                    title: "boq_name"
                }

            },
            callback: {
                onClick: function(event, treeId, treeNode) {
                    var url = '../cont/contInfo/contBoqXM_getcontBoqId.action?contBoq.id=' + treeNode.id + '&contBoq.proj_code=' + treeNode.proj_code + '&t=' + Math.random();
                    var bframe = document.getElementById('boqInfoFrame').contentWindow.document;
                    $.post(url, function(data) {

                        $("#budg_code", bframe).html(data.budg_code);
                        $("#boq_code", bframe).html(data.boq_code);
                        $("#boq_name", bframe).html(data.boq_name);
                        $("#boq_unit", bframe).html(data.boq_unit);
                        $("#start_stack", bframe).html(data.start_stack);
                        $("#end_stack", bframe).html(data.end_stack);
                        $("#site", bframe).html(data.site);
                        $("#detail_no", bframe).html(data.detail_no);
                        $("#draw_book", bframe).html(data.draw_book);
                        $("#draw_no", bframe).html(data.draw_no);
                        $("#memo", bframe).html(data.memo);

                        $("#main_qty", bframe).html(data.main_qty);
                        $("#main_price", bframe).html(data.main_price);
                        $("#main_money", bframe).html(data.main_money);
                        $("#qty", bframe).html(data.qty);
                        $("#price", bframe).html(data.price);
                        $("#money", bframe).html(data.money);
                        $("#upd_qty", bframe).html(data.upd_qty);
                        $("#upd_price", bframe).html(data.upd_price);
                        $("#upd_money", bframe).html(data.upd_money);
                    }, "json");
                    addAlterBoq.show_btn();
                    if (treeNode) {
                        if (treeNode.parent_flag == -1) {
                            addAlterBoq.disble_btn(1);
                        }
                        if (treeNode.parent_flag != -1) {
                            addAlterBoq.disble_btn(2);
                            addAlterBoq.disble_btn(4);
                        }
                        if (treeNode.parent_flag == 0 && treeNode.alter_flag != 4) {
                            addAlterBoq.disble_btn(3);
                        }
                        if (treeNode.alter_flag != 4 && treeNode.parent_flag != 1) {
                            addAlterBoq.disble_btn(3);
                        }
                        if (treeNode.alter_flag != 4) {
                            addAlterBoq.disble_btn(5);
                        }
                    }


                    return false;
                },
                onRightClick: function(event, treeId, treeNode) {
                    //  if (treeNode && treeNode.pay_item_flag != 1) {
                    var treeObj = $.fn.zTree.getZTreeObj("tree");
                    treeObj.selectNode(treeNode, false);
                    addAlterBoq.show_r_menu();
                    addAlterBoq.show_btn();
                    if (treeNode) {
                        if (treeNode.parent_flag == -1) {
                            addAlterBoq.disble_r_menu(1);
                            addAlterBoq.disble_btn(1);

                        }
                        if (treeNode.parent_flag != -1) {
                            addAlterBoq.disble_r_menu(2);
                            addAlterBoq.disble_r_menu(4);
                            addAlterBoq.disble_btn(2);
                            addAlterBoq.disble_btn(4);
                        }
                        if (treeNode.parent_flag == 0 && treeNode.alter_flag != 4) {
                            addAlterBoq.disble_r_menu(3);
                            addAlterBoq.disble_btn(3);
                        }
                        if (treeNode.alter_flag != 4 && treeNode.parent_flag != 1) {
                            addAlterBoq.disble_r_menu(3);
                            addAlterBoq.disble_btn(3);
                        }
                        if (treeNode.alter_flag != 4) {
                            addAlterBoq.disble_r_menu(5);
                            addAlterBoq.disble_r_menu(6);
                            addAlterBoq.disble_btn(5);
                        }
                        rMenu_.show({top: event.pageY, left: event.pageX});

                    }
                    //  }
                },
                onAsyncSuccess: function(event, treeId, treeNode, msg) {
                    //  var treeObj = $.fn.zTree.getZTreeObj("tree");
                    //treeObj.expandAll(true)
                }
            },
            view: {
                nameIsHTML: true,
                selectedMulti: false,
                addDiyDom: addAlterBoq.addDiyDom
            }
        });


    },
    addcld: function(masterId) {
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        var nodes = treeObj.getSelectedNodes();
        var node = nodes[nodes.length - 1];
        if (!node) {
            return;
        }
        if (node.pay_item_flag == 0 || node.alter_flag == 4) {//是支付项
            var res = window.showModalDialog("../alter/alter_addContBoq.action?cld=1&masterId=" + masterId + "&id=" + node.id + "&projCode=" + projCode_ + "&type=3&pid=" + node.getParentNode().id + "&r=" + Math.random(), "", "dialogHeight=550px;dialogWidth=1050px");
            res = eval(res);
            if (res) {
                var addNode = [{boq_name: res[0].boq_name, id: res[0].id, pay_item_flag: res[0].pay_item_flag,
                        alter_flag: res[0].alter_flag, cont_id: res[0].cont_id, boq_id: res[0].boq_id,
                        boq_pid: res[0].boq_pid, bid_id: res[0].bid_id, code_name: addAlterBoq.createCodeName(res[0]), proj_code: res[0].proj_code, parent_flag: res[0].parent_flag
                    }];
                treeObj.addNodes(node, addNode);
                addNode = treeObj.getNodeByParam("id", res[0].id, null);
                node.pay_item_flag = 0;
                treeObj.selectNode(addNode, false);
                var bframe = document.getElementById('boqInfoFrame').contentWindow.document;
                $("#f2", bframe).attr("src", "../alter/alter_jumpAlterBoq.action?masterId=" + masterId + "&r=" + Math.random());
            }
        } else {
            //alert("请选择非支付项节点或新增支付项节点！");
        }

    },
    addP: function(masterId) {
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        var nodes = treeObj.getSelectedNodes();
        var node = nodes[nodes.length - 1];
        if (!node) {
            return;
        }
        if (node.pay_item_flag == 1) {//是支付项
            var res = window.showModalDialog("../alter/alter_addContBoq.action?masterId=" + masterId + "&id=" + node.id + "&type=3&projCode=" + projCode_ + "&r=" + Math.random(), "", "dialogHeight=550px;dialogWidth=1050px");
            res = eval(res);
            if (res) {
                var addNode = [{boq_name: res[0].boq_name, id: res[0].id, pay_item_flag: res[0].pay_item_flag,
                        alter_flag: res[0].alter_flag, cont_id: res[0].cont_id, boq_id: res[0].boq_id,
                        boq_pid: res[0].boq_pid, bid_id: res[0].bid_id, code_name: addAlterBoq.createCodeName(res[0]), proj_code: res[0].proj_code, parent_flag: res[0].parent_flag
                    }];
                treeObj.addNodes(node.getParentNode(), addNode);
                addNode = treeObj.getNodeByParam("id", res[0].id, null);
                treeObj.moveNode(node, addNode, "next", false);
                var bframe = document.getElementById('boqInfoFrame').contentWindow.document;
                $("#f2", bframe).attr("src", "../alter/alter_jumpAlterBoq.action?masterId=" + masterId + "&r=" + Math.random());
            }
        } else {
            //alert("请选择支付项节点！");
        }
    },
    upd: function(masterId) {
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        var nodes = treeObj.getSelectedNodes();
        var node = nodes[nodes.length - 1];
        if (node.alter_flag == 4) {
            var res = [];
            if (node.pay_item_flag == 0) {
                res = window.showModalDialog("../alter/alter_addContBoq.action?upd=1&masterId=" + masterId + "&id=" + node.id + "&projCode=" + projCode_ + "&type=1&r=" + Math.random(), "", "dialogHeight=550px;dialogWidth=1050px");
            } else {
                res = window.showModalDialog("../alter/alter_addContBoq.action?upd=1&masterId=" + masterId + "&id=" + node.id + "&projCode=" + projCode_ + "&type=3&r=" + Math.random(), "", "dialogHeight=550px;dialogWidth=1050px");

            }
            if (res) {
                res = eval(res);
                node.boq_name = res[0].boq_name;
                node.id = res[0].id;
                node.pay_item_flag = res[0].pay_item_flag;
                node.alter_flag = res[0].alter_flag;
                node.boq_id = res[0].boq_id;
                node.bid_id = res[0].bid_id;
                node.boq_pid = res[0].boq_pid;
                node.parent_flag = res[0].parent_flag;
                node.code_name = addAlterBoq.createCodeName(node);
                treeObj.selectNode(treeObj.getSelectedNodes()[0], false);
                var bframe = document.getElementById('boqInfoFrame').contentWindow.document;
                $("#f2", bframe).attr("src", "../alter/alter_jumpAlterBoq.action?masterId=" + masterId + "&r=" + Math.random());
            }

        } else {
            // alert("原合同清单项不能修改");
        }
    },
    del: function() {
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        var nodes = treeObj.getSelectedNodes();
        var node = nodes[nodes.length - 1];

        if (node.alter_flag == 4) {
            if (confirm("确认删除？")) {
                loding.show();
                $.post("../alter/alter_delContBoq.action", {
                    projCode: projCode_,
                    id: node.id,
                    boqId: node.boq_id,
                    contId: node.cont_id,
                    boqPid: node.boq_pid,
                    bidId: node.bid_id,
                    Pay_item_flag: node.pay_item_flag
                }, function(d) {
                    if (d == 1) {
                        alert("删除成功");
                        treeObj.removeNode(node, null);
                        loding.hidden();
                    } else {
                        alert("删除失败");
                        loding.hidden();
                    }

                }, "text");
            }
        } else {
            //alert("原合同清单项不能删除");
        }

    },
    /**
     * 添加分项工程
     * @returns {void}
     */
    add_fxgc: function(mid) {
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        var nodes = treeObj.getSelectedNodes();
        var node = nodes[nodes.length - 1];
        if (!node || !node.getParentNode()) {
            return;
        }
        if (node.parent_flag == 1 || node.parent_flag == -1) {
            return;
        }
        //treeObj.expandNode(node, true, false, false, true);

        if (node.pay_item_flag == 0) {//不是支付项
            var res = window.showModalDialog("../alter/alter_addContBoq.action?masterId=" + mid + "&cld=1&type=1&id=" + node.id + "&projCode=" + projCode_ + "&pid=" + node.getParentNode().id + "&r=" + Math.random(), "", "dialogHeight=550px;dialogWidth=1050px");
            if (res) {
                res = eval(res);
                var cname = res[0].boq_name;

                var addNode = [{boq_name: res[0].boq_name, id: res[0].id, pay_item_flag: res[0].pay_item_flag,
                        alter_flag: res[0].alter_flag, cont_id: res[0].cont_id, boq_id: res[0].boq_id,
                        boq_pid: res[0].boq_pid, bid_id: res[0].bid_id, code_name: cname, parent_flag: res[0].parent_flag
                    }];
                treeObj.addNodes(node, addNode);
                addNode = treeObj.getNodeByParam("id", res[0].id, null);
                treeObj.selectNode(addNode, false);
            }

        } else {
            // alert("请选择非支付项节点！");
        }

    },
    /**
     * 变更
     * @returns {void}
     */
    addBoq: function(masterId) {
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        var nodes = treeObj.getSelectedNodes();
        var node = nodes[nodes.length - 1];
        if (node.pay_item_flag == 1) {
            var alterFlag = node.alter_flag
            if (!node.alter_flag) {
                alterFlag = 0;
            }
            var res = window.showModalDialog("../alter/alter_jumpAddBoq.action?projCode=" + projCode_ + "&id=" + node.id + "&boq_id=" + node.boq_id + "&masterId=" + masterId + "&alter_flag=" + alterFlag + "&r=" + Math.random(), "", "dialogHeight=550px;dialogWidth=1050px");
            var bframe = document.getElementById('boqInfoFrame').contentWindow.document;
            $("#f2", bframe).attr("src", "../alter/alter_jumpAlterBoq.action?masterId=" + masterId + "&r=" + Math.random());
            node.code_name = addAlterBoq.createCodeName(node, "red");
            treeObj.updateNode(node);
            treeObj.selectNode(node, false);
            if (res == 2 || res == 3) {
                treeObj.reAsyncChildNodes(node.getParentNode(), "refresh");
            }
        } else {
            //   alert("请选择支付项节点！");
        }
    },
    createCodeName: function(node, cl) {
        var codename = "";
        if (node.chapter_code == '400')
        {
            if (node.site != null)
            {
                codename += "[" + node.site + "]" + " ";
            }

        } else {
            if (node.start_stack != null && node.end_stack != null)
            {
                codename += "[" + node.start_stack + "~" + node.end_stack + "]" + " ";
            }
            if (node.start_stack != null && node.end_stack == null)
            {
                codename += "[" + node.start_stack + "]" + " ";
            }
        }
        codename = "<span style='color:#996600;'>" + codename + "</span>";
        var cname = "<span style='color:green;'>" + node.boq_code + " " + node.boq_name + "</span>";
        if (cl) {
            cname = "<span style='color:red;'>" + node.boq_code + " " + node.boq_name + "</span>";
        }
        codename += cname;
        return codename;
    },
    /**
     *  禁用右键菜单第 i 项
     * @param {int} i 
     * @returns {undefined}
     */
    disble_r_menu: function(i) {
        $(".l-menu-inner div[ligeruimenutemid=" + i + "]").attr("class", "l-menu-item l-menu-item-disable");
    },
    /**
     * 全部启用右键
     * @returns
     */
    show_r_menu: function() {
        var disables = $(".l-menu-item-disable");
        if (disables) {
            disables.each(function(i, obj) {
                $(obj).attr("class", "l-menu-item");
            });
        }
    },
    /**
     *  禁用第N个按钮
     * @returns 
     */
    disble_btn: function(n) {

        $(".l-toolbar-item-hasicon").each(function(i, Obj) {
            if (i + 1 == n) {
                $(Obj).attr("class", "l-toolbar-item l-panel-btn l-toolbar-item-hasicon l-toolbar-item-disable");
                $(Obj).mouseover(function() {
                    $(Obj).attr("class", "l-toolbar-item l-panel-btn l-toolbar-item-hasicon l-toolbar-item-disable");
                });
            }
        });
    },
    /**
     *  按钮全部启用
     * @returns
     */
    show_btn: function() {
        $(".l-toolbar-item-hasicon").each(function(i, Obj) {
            $(Obj).attr("class", "l-toolbar-item l-panel-btn l-toolbar-item-hasicon");
            $(Obj).mouseover(function() {
                $(Obj).attr("class", "l-toolbar-item l-panel-btn l-toolbar-item-hasicon l-panel-btn-over");
            });
        });
    }
};
var addAlterBoqRight = {
    init: function(data_) {
        var alterType = $("#alterType").ligerComboBox({
            data: data_,
            textField: "type_name",
            valueField: "type_id",
            width: 150,
            height: 23,
            selectBoxWidth: 150,
            onSelected: function(value, text) {
                switch (text) {
                    case "数量变更":
                        $("#bgTab tr:gt(0)").remove();
                        $("#bgTab").append("<tr><th>增减数量</th><td colspan=\"2\"><input name=\"delta_qty\" id=\"bg_int_c\" required=\"required\" pattern=\"^[-+]*\\d+$\" /></td></tr>");
                        break;
                    case "价格变更":
                        $("#bgTab tr:gt(0)").remove();
                        $("#bgTab").append("<tr><th>增减价格</th><td colspan=\"2\"><input name=\"delta_money\" id=\"bg_int_m\" title=\"请输入正确的增减价格,例：-3.5\" required=\"required\" pattern=\"^[-+]*\\d+(\\.\\d{1,2})?$\" /></td></tr>");
                        break;
                    case "数量和价格均变更":
                        $("#bgTab tr:gt(0)").remove();
                        $("#bgTab").append("<tr><th>增减数量</th><td><input name=\"delta_qty\" id=\"bg_int_c\" required=\"required\" pattern=\"^[-+]*\\d+$\"/></td><th>增减价格</th><td><input name=\"delta_money\" id=\"bg_int_m\" required=\"required\" title=\"请输入正确的增减价格,例：-3.5\" required=\"required\" pattern=\"^[-+]*\\d+(\\.\\d{1,2})?$\" /></td></tr>");
                        break;
                    case "新增项目变更":
                        $("#bgTab tr:gt(0)").remove();
                        $("#bgTab").append("<tr><th>增减数量</th><td><input name=\"delta_qty\" id=\"bg_int_c\" required=\"required\" pattern=\"^[-+]*\\d+$\"/></td><th>增减价格</th><td><input name=\"delta_money\" id=\"bg_int_m\" required=\"required\" title=\"请输入正确的增减价格,例：-3.5\" required=\"required\" pattern=\"^[-+]*\\d+(\\.\\d{1,2})?$\" /></td></tr>");
                        break;
                }
            },
            onSuccess: function() {
                // this.selectValue(this.data[0].type_id);
            }
        });
        alterType && alterType.selectValue(alterType.data[0].type_id);
    },
    submet: function() {
        if ($("#contBoqId").val() == "") {
            alert("请先选择一个支付项");
            return false;
        }
        if ($("#bg_int_c").val()) {
            if (!/^[-+]*\d+$/.test($("#bg_int_c").val())) {
                alert("请输入正确的数量\r\n例如：-10或+20");
                return false;
            }
        }
        if ($("#bg_int_m").val()) {
            if (!/^[-+]*\d+(\.\d{1,2})?$/.test($("#bg_int_m").val())) {
                alert("请输入正确的价格\r\n例如：-10.5或+20");
                return false;
            }
        }
        $("#boqForm").ajaxSubmit({
            success: function(v) {
                if (v >= 0) {
                    alert("操作成功！");
                    parent.parent.window.parent.returnValue = 1;
                    window.location.reload();
                } else {
                    alert("操作失败，请重试!");
                }
            }
        });
        return false;
    }
};
