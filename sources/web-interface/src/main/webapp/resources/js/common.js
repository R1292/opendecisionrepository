/*
 * Open Decision Repository
 *
 * Ben Ripkens
 * bripkens.dev@gmail.com
 */




/*
 * Definition of our namespaces
 */
var odr = odr || {};



/*
 * Required to use jQuery with icefaces
 */
var j = jQuery.noConflict();


/*
 * Some settings
 */
odr.textareaResizerDelta = 70;
odr.textareaResizerMin = 40;






/*
 * This function will be called after the document has been loaded completely
 */
odr.init = function() {
    odr.prefill = new odr.Prefill();
    odr.prefill.init();

    odr.toggling = new odr.Toggling();
    odr.toggling.init();

    odr.textareaResizer = new odr.TextareaResizer();
    odr.textareaResizer.init();

    odr.popup = new odr.Popup();
    odr.popup.init();

    // anchor fix for base.href
    j("a.anchorLink").each(function() {
        element = j(this);
        href = window.location.href;
        element.attr("href", window.location.href.split("#")[0] + element.attr("href"));
    });

    j(".initDisabled").each(function() {
        j(this).attr("disabled", true);
    });


    odr.decisionWizardQuickAdd();

    odr.feedbackpopup();
    jQuery('input').keyup(function(evt){
        if(evt.keyCode == 13) {
            jQuery(this).parents('form').find('input.enterkeybutton').click();
        }
    });
}




/*
 * Get started!
 *
 * please dont't place any custom code in this function.
 * Please use the odr.init instead.
 */
j(document).ready(function() {
    odr.init();
});








/*
 * This class allows to have prefill values by assigned labels (preferably)
 * 1) Class "prefill" to an input field  (input.prefill)
 * 2) Class "prefillValue" to an element (mostly label) which reference the input field
 *    with the "for" attribute.
 * 3) The prefill text will be obtained from the element, which has the class prefillValue
 */
odr.Prefill = function() {
    this.prefillValue = new Array();

    this.init = function() {
        j("input.prefill").each(function() {
            var element = j(this);
            var id = element.attr("id");
            var text = j(".prefillValue[for=" + id + "]").text();

            odr.prefill.prefillValue[id] = text;

            element.focus(odr.prefill.inputFocus);

            element.blur(odr.prefill.inputBlur);

            odr.prefill.prefill(element);
        });
    }

    this.inputFocus = function() {
        var element = j(this);

        if (element.val() == odr.prefill.prefillValue[element.attr("id")]) {
            element.val("");
            element.removeClass("prefilled");
        }
    }

    this.inputBlur = function() {
        var element = j(this);

        odr.prefill.prefill(element);
    }

    this.prefill = function(element) {
        if (element.val() == "") {
            element.val(odr.prefill.prefillValue[element.attr("id")]);
            element.addClass("prefilled");
        }
    }
}











/*
 * This class enables toggling functionality
 * 1) Add togglelink class to a hyperlink
 *    Reference the id of the element that you want to toggle in the
 *    name attribute
 * Done!
 *
 * An extension allows to have cancel buttons in a form that might be displayed.
 * 1) Assign the togglelink class to an input element. The input element must be
 *    a child of the container that is to be toggled.
 * Done!
 */
odr.Toggling = function() {

    this.init = function() {
        j("a.togglelink").each(function() {
            var button = j(this);
            var name = button.attr("name");
            var element = j("#" + name);

            button.click(function() {
                odr.toggling.toggleSlideElement(element);

                return false;
            });

            element.children("input.togglelink").first().click(function() {
                odr.toggling.toggleSlideElement(element);

                return false;
            });
        });
    }

    this.toggleSlideElement = function(element) {
        if (!odr.isVisible(element)) {
            element.slideDown();
        } else {
            element.slideUp();
        }
    }

    this.toggleSlide = function(elementSelector) {
        odr.toggling.toggleSlideElement(j(elementSelector));
    }

    this.slideDown = function(elementSelector) {

        element = j(elementSelector);
        if (!odr.isVisible(element)) {
            element.slideDown();
        }
    }

    this.toggleFadeElement = function(element) {
        if (!odr.isVisible(element)) {
            element.fadeIn();
        } else {
            element.fadeOut();
        }
    }

    this.toggleFade = function(elementSelector) {
        odr.toggling.toggleFadeElement(j(elementSelector));
    }
}








/*
 * The JavaScript functionality behind odr:inputTextareaResizer
 */
odr.TextareaResizer = function() {

    this.init = function() {
        j("div.textareaBigger").click(function() {
            var element = j(this).siblings("textarea");

            odr.textareaResizer.bigger(element);
        })
        j("div.textareaSmaller").click(function() {
            var element = j(this).siblings("textarea");

            odr.textareaResizer.smaller(element);
        });
    }


    this.bigger = function(element) {
        var newheight = element.height() + odr.textareaResizerDelta;

        element.animate({
            height:newheight + "px"
        }, 300);
    }

    this.smaller = function(element) {
        var newheight = element.height() - odr.textareaResizerDelta;

        if (newheight < odr.textareaResizerMin) {
            newheight = odr.textareaResizerMin;
        }

        element.animate({
            height:newheight + "px"
        }, 300);
    }
}



/*
 * This class allows to show modal popups
 * 1) Add "modalPopupLink" class to a link that should open the popup
 *    Reference the element that should be faded in with the name attribute
 *    of the link
 * 2) Add the "modalPopup" class to the element that should be shown
 * 3) Add "hideModalPopupLink" as a class to all links that should close
 *    the popup
 * 4) Create an element with the id "backgroundPopup"
 *    This element will be used as an overlay. A click on this element
 *    will close all popups
 */
odr.Popup = function() {
    
    this.init = function() {
        j(".modalPopupLink").click(function() {
            this.showModalPopup(j(this).attr("name"));
        })

        j(".hideModalPopupLink").click(this.hide);

        j("#backgroundPopup").click(this.hide);
    }

    this.show = function(id) {
        var windowWidth = j(document).width();
        var windowHeight = j(document).height();
        var popupHeight = j("#" + id).height();
        var popupWidth = j("#" + id).width();

        j("#backgroundPopup").css({
            "height": windowHeight,
            "width": windowWidth
        });

        j("#" + id).css({
            "top": windowHeight/2-popupHeight/2,
            "left": windowWidth/2-popupWidth/2
        });

        j("#backgroundPopup").fadeIn("slow");
        j("#" + id).fadeIn("slow");
    }

    this.hide = function() {
        j(".modalPopup").fadeOut("slow");
        j("#backgroundPopup").fadeOut("slow");
    }
}








/*
 * Some helper functions
 */
odr.isVisible = function(element) {
    return element.is(":visible");
}

odr.refresh = function() {
    location.reload(true);
}

odr.texEncode = function(content) {
    var openingBracketPlaceholder = "!\"!{!\"!";
    var closingBracketPlaceholder = "!\"!}!\"!";

    content = content.replace("{", openingBracketPlaceholder);
    content = content.replace("}", closingBracketPlaceholder);

    content = content.replace("\\", "{\\backslash}");
    content = content.replace("#", "{\\#}");
    content = content.replace("$", "{\\$}");
    content = content.replace("%", "{\\%}");
    content = content.replace("&", "{\\&}");
    content = content.replace("~", "{\\~}");
    content = content.replace("_", "{\\_}");
    content = content.replace("^", "{\\^}");

    content = content.replace(openingBracketPlaceholder, "{\\{}");
    content = content.replace(closingBracketPlaceholder, "{\\}}");

    return content;
}

odr.htmlEncode = function(value){
    return $('<div/>').text(value).html();
}

odr.htmlDecode = function(value){
    return $('<div/>').html(value).text();
}

/*
 * This method enables JavaScript based preselection
 * the fix may be used when it is not possible
 * to have a preselected value using JSF (or any
 * other server side technology)
 *
 * 1) Place an "a" element right before the select.
 *    The value of the option that should be selected
 *    is referenced by the name attribute of the "a"
 *    element.
 *
 * E.g.
 * <a name="c" />
 * <select>
 *     <option value="a">1</option>
 *     <option value="b">2</option>
 *     <option value="c">3</option>
 * </select>
 *
 * Outcome:The third option will be selected upon page load
 */
odr.preselect = function() {
    j("a.preselectValue").each(function() {
        var sourceElement = j(this);
        var optionToSelect = sourceElement.attr("name");

        var selectElement = sourceElement.next("select");

        var listener = selectElement.attr("onchange");
        selectElement.removeAttr("onchange");

        selectElement.children("option:selected").removeAttr("selected");
        selectElement.children("option[value=" + optionToSelect + "]").attr("selected", "selected");

        selectElement.attr("onchange", listener);
        selectElement.focus();
        selectElement.blur();

        selectElement.change(function() {
            var selectElement = j(this);
            var previousElement = selectElement.prev();

            var selectedOption = selectElement.children("option:selected").attr("value");
            previousElement.attr("name", selectedOption);
        });
    });
}



/*
 * Some functions that are called by the icefaces framework through the JsfUtil
 */
odr.hideModalPopup = function() {
    odr.popup.hide();
}

odr.hideDecisionAddForm = function() {
    odr.toggling.toggleSlide("#decisionQuickAddContainer");
}
odr.showIterationDeleteForm = function() {
    odr.showDeleteDialog();
    j( "#deletionPopup" ).dialog( "option", "title", 'Delete iteration' );
}
odr.showDecisionDeleteForm = function() {
    odr.popup.show("deleteDecisionConfirmationPopup");
}

odr.showProjectDeleteForm = function() {
    odr.popup.show("deleteProjectConfirmationPopup");
}

odr.showConcernDeleteForm = function() {
    odr.showDeleteDialog();
    j( "#deletionPopup" ).dialog( "option", "title", 'Delete concern' );
}

odr.showVersionDeleteForm = function() {
    odr.showDeleteDialog();
    j( "#deletionPopup" ).dialog( "option", "title", 'Delete version' );
}

odr.showMemberRemoveForm = function(){
    odr.showDeleteDialog();
    j( "#deletionPopup" ).dialog( "option", "title", 'Remove member' );
}

odr.showProjectRemoveForm = function(){
    odr.showDeleteDialog();
    j( "#deletionPopup" ).dialog( "option", "title", 'Remove project' );
}

odr.showDeleteDialog = function(){
    j("#deletionPopup" ).dialog({
        resizable: false,
        dialogClass: 'membersdialog',
        height:150,
        draggable: false,
        minWidth: 360,
        maxWidth:360,
        closeOnEscape: true,
        modal: true
    });


}
odr.datetimepickerValidationFix = function() {
    j(".datetimepickerDateInput").focus().blur();
}
odr.decisionWizardSearchOne = function(start) {
    if (start) {
        j('#ajaxLoaderBar').css('display', 'block');
    } else {
        j('#ajaxLoaderBar').css('display', 'none');
    }

    odr.decisionWizardStepOneDisableRetrieve(start);
}
odr.decisionWizardStepOneDisableRetrieve = function(disable) {
    if (disable) {
        j("#manageDecisionSidebarForm\\:inSelectExternal").attr("disabled", true);
        j("#manageDecisionSidebarForm\\:manageDecisionSelectExternalId").attr("disabled", true);
    } else {
        j("#manageDecisionSidebarForm\\:inSelectExternal").removeAttr("disabled");
        j("#manageDecisionSidebarForm\\:manageDecisionSelectExternalId").removeAttr("disabled");
    }
}
odr.setCurrentTime = function() {
    var src = j(window.event.srcElement);

    var now = new Date();

    var changes = [
    {
        selector : "datetimepickerMonth",
        newIndex : now.getMonth()
    },
    {
        selector : "datetimepickerDay",
        newIndex : now.getDate() - 1
    },
    {
        selector : "datetimepickerHour",
        newIndex : now.getHours()
    },
    {
        selector : "datetimepickerMinute",
        newIndex : now.getMinutes()
    }
    ];

    for (var i = 0; i < changes.length; i++) {
        src.siblings("." + changes[i].selector + ":first").children().removeAttr("selected").eq(changes[i].newIndex).
        attr("selected", "selected").hide().show();
    }
    
    src.siblings(".datetimepickerYear:first").children().removeAttr("selected").filter('[value="' + (now.getYear() + 1900) + '"]').
    attr("selected", "selected").hide().show();
}



odr.decisionWizardQuickAdd = function() {
    var dialog = j("#wizardQuickAddDecision");

    if (dialog.length == 0) {
        return;
    }

    dialog.dialog({
        autoOpen: false,
        height: 208,
        width: 618,
        modal: true,
        resizable : false,
        dialogClass : "wizardQuickAddDialog",
        close : function() {
            j("input#wizardQuickAddForm\\:inQuickDecisionName").val("");
        }
    });
}

/**
 * set the background of every second row to css class "odd"
 */
odr.colorrow = function(){
    j("tr").not(".subRow").filter(":odd").addClass("odd");
}

/**
 * Feedback popup
 */
odr.feedbackpopup = function() {
    j("#feedbackPopup").dialog({
        autoOpen: false,
        height: 345,
        width: 618,
        modal: true,
        resizable : false,
        close : function() {
            j("input#feedbackInEmail").val("");
            j("textarea#feedbackInMessage").val("").removeClass("error");
        }
    });

    j("#feedbackMenu").click(function() {
        j("#feedbackPopup").dialog("open");
    });

    j("#feedbackButtonCancel").click(function() {
        j("#feedbackPopup").dialog("close");
        return false;
    });

    j("#feedbackButtonSend").click(function() {
        var textarea = j("textarea#feedbackInMessage");
        
        if (textarea.val().length < 5) {
            textarea.addClass("error");
            return false;
        }

        j("#feedbackInUrl").val(window.location);
        j("#feedbackInPage").val(j("html:first").html());

        return true;
    });
};