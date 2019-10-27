function checkData(text_field) {

    let id = text_field.id;

    let value = $("#"+id).val().replace(/,/g, '.');

    if (!$.isNumeric(value) && value != "-" && value != "") {
        $("#"+id).val("");
        $("#"+ id +"_wrongAlert").stop(true, true).fadeIn( 200 ).delay( 1000 ).fadeOut( 200 );
    }

    if ((-3 > value) || (value > 3)) {
        $("#"+id).val("");
        //$("#x").attr("style", 'border: 2px solid red; mix-blend-mode: normal;');
        $("#"+ id +"_wrongAlert").stop(true, true).fadeIn( 200 ).delay( 1000 ).fadeOut( 200 );
    }
}

// function checkFields() {
//     let value = $("#x").val().replace(/,/g, '.');
//     let correctFields = true;
//     if (value.length === 0) {
//         correctFields = false;
//     }

//     if (!($('input[type="radio"]').is(":checked") || $('#y option').is(":selected"))) {
//         correctFields = false;
//     }

//     if (!$("#r").val()) {
//         correctFields = false;
//     }

//     if (correctFields) {
//         $("#x").val(value);
//         $('.mainTable').addClass("zoomOut");
//     } else {
//         event.preventDefault();
//         $('#wrongInputAlert').stop(true, true).fadeIn( 300 ).delay( 1500 ).fadeOut( 400 );
//     }
// }

function histBtnPressed() {
    $(".arrow-icon").toggleClass("open");
    if($("#histWrap").hasClass("active")) {
        hideTable();
    } else {
        showTable();
    }
}

function showTable() {
    
    let rows = getHistory();

    if(rows.length == 0) {
        $('#histMessage').stop(true,true).fadeIn( 300 ).delay( 1000 ).fadeOut( 300 );
        return;
    }
    $(".extendBtn").toggleClass("active");
    $("#historyTableBody").html(rows.join(""));
    $("#histWrap").toggleClass("active");
}

function hideTable() {
    $(".extendBtn").toggleClass('active');
    $("#histWrap").toggleClass("active");
    $('#dot').stop(true,true).fadeOut();
    $('#arrow').stop(true,true).fadeOut();
}

function equal(obj, newObj) {
    return (obj.x === newObj.x) && (obj.y === newObj.y) && (obj.r === newObj.r) && (obj.result === newObj.result);
}

function save(x, y, r, result) {
    let obj = {
        x: x,
        y: y,
        r: r,
        result: result
    };
    let allData = [];
    if (localStorage.userData) {
        previousObjs = localStorage.getItem('userData');
        allData.push(previousObjs);
        if (allData.length == 0 || !equal(JSON.parse(localStorage.getItem('lastObj')), obj)) {
            localStorage.setItem('lastObj', JSON.stringify(obj));
            allData.push(JSON.stringify(obj));
            localStorage.setItem('userData', allData);
        }
    } else {
        allData.push(obj);
        localStorage.setItem('lastObj', JSON.stringify(obj));
        localStorage.setItem('userData', JSON.stringify(obj));
    }
}

function getHistory() {
    let rxp = /{([^}]+)}/g,
        curMatch;
    let rows = [], j = -1;
    if ($('#history').text()) {
        userAttempts = $('#history').text();
        while (curMatch = rxp.exec(userAttempts)) {
                        obj = JSON.parse("{" + curMatch[1] + "}");
                        let x = obj.x.toString().replace(".", ",").length > 15 ?
                            (obj.x.toString().replace(".", ",").substring(0, 15) + "…") :
                            obj.x.toString().replace(".", ",");
                        let y = obj.y.toString().replace(".", ",").length > 15 ?
                            (obj.y.toString().replace(".", ",").substring(0, 15) + "…") :
                            obj.y.toString().replace(".", ",");
                        let r = obj.r.toString().replace(".", ",").length > 15 ?
                            (obj.r.toString().replace(".", ",").substring(0, 15) + "…") :
                            obj.r.toString().replace(".", ",");
                        rows[++j] = '<tr height="30px"><td width="130px" >';
                        rows[++j] = '<span title=\"' + obj.x.toString() + '\"' + '>' + x + '</span>';
                        rows[++j] = '</td><td width="130px" >';
                        rows[++j] = '<span title=\"' + obj.y.toString() + '\"' + '>' + y + '</span>';
                        rows[++j] = '</td><td width="130px" >';
                        rows[++j] = '<span title=\"' + obj.r.toString() + '\"' + '>' + r + '</span>';
                        rows[++j] = '</td><td width="130px" >';
                        rows[++j] = obj.result === "true" ? '<span style="color:#008000;text-align:center;">Попадание</span>' : '<span style="color:#B22222;text-align:center;">Промах</span>';
                        rows[++j] = '</td></tr>';
        }
    }
    return rows;
}

function clearHist() {
    localStorage.clear();
    hideTable();
}

function setTime() {
    $("#time").html(new Date().toLocaleTimeString());
}

function okPressed() {
    $('.mainTable').addClass("zoomOut");
}

function setDotCoord(x, y, r) {
    let dot = document.getElementById("dot");
    //dot.style.cx = 100 + 70 * (x/r);
    //dot.style.cy = 100 - 70 * (y/r);
    $("#dot").attr("cx", 100 + 70 * (x/r));
    $("#dot").attr("cy", 100 - 70 * (y/r));
}

function setDotCoordResponse() {
    if(!($("#xField").length && $("#yField").length && $("#rField").length)) return;
    let x = parseFloat($("#xField").attr("title").replace(",", "."));
    let y = parseFloat($("#yField").attr("title").replace(",", "."));
    let r = parseFloat($("#rField").attr("title").replace(",", "."));
    setDotCoord(x, y, r);
    showDot();
}

function makeRowsClickable() {
    let table = document.getElementById("historyTableBody");
    for (let i = 0, row; row = table.rows[i]; i++) {
        row.setAttribute("onclick", "rowClicked(this);");
    }
}


function rowClicked(row) {
    let children = row.children;
    let x = parseFloat(children[0].firstChild.title);
    let y = parseFloat(children[1].firstChild.title);
    let r = parseFloat(children[2].firstChild.title);
    setDotCoord(x, y, r);
    showDot();
}

function showDot() {
    let dot = document.getElementById("dot");
    let arrow = document.getElementById("arrow");
    let x = $("#dot").attr("cx");
    let y = $("#dot").attr("cy");
    if(x < 0 || y < 0 || x > 200 || y > 200) {
        $('#dot').stop(true,true).fadeOut();
        $('#arrow').stop(true,true).fadeOut();
        $('#arrow').attr("transform", getTransform(x,y));
        $('#arrow').stop(true,true).fadeIn();
    } else {
        $('#dot').stop(true,true).fadeIn();
        $('#arrow').stop(true,true).fadeOut();
    }
}

class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}

class Line {
    constructor(x1, y1, x2, y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.k = (y2-y1)/(x2-x1);
        this.b = (x2*y1 - x1*y2)/(x2 - x1);
    }

    intersect(other) {
        if(other.x1==other.x2) {
            return(new Point(other.x1, this.k*other.x1+this.b))
        } else if (other.y1==other.y2) {
            return(new Point((other.y1-this.b)/this.k, other.y1));
        } else return(new Point(0,0));
    }
}

class Transform {
    constructor(x, y, rot) {
        this.x = x;
        this.y = y;
        this.rot = rot;
    }

    setPoint(point) {
        this.x = point.x;
        this.y = point.y;
    }

    toString() {
        return "translate(" + this.x + ", " + this.y + ") rotate(" + this.rot + ")";
    }
}

function getTransform(x, y) {
    let result = new Transform(100, 100, 0);
    let line = new Line(100, 100, x, y);
    let degree = Math.atan(line.k) * 180/Math.PI;
    if(x > 100) degree += 180;
    result.rot = degree;
    if(degree >= -45 && degree <= 45) {
        result.setPoint(line.intersect(new Line(0,0,0,200)));
    }
    if(degree >= 45 && degree <= 135) {
        result.setPoint(line.intersect(new Line(0,0,200,0)));
    }
    if(degree >= 135 && degree <= 225) {
        result.setPoint(line.intersect(new Line(200,0,200,200)));
    }
    if(degree >= 225 && degree <= 315) {
        result.setPoint(line.intersect(new Line(0,200,200,200)));
    }
    return result.toString();
}

function imageClicked(event) {

    let val = $("#r").val();
    if(!val) {
        $("#r")[0].reportValidity();
        return;
    }

    //console.log(event.clientX + " " + event.clientY);

    let relativeX = event.pageX - $('#imageSvg').offset().left;
    let relativeY = event.pageY - $('#imageSvg').offset().top;

    //console.log(relativeX + " " + relativeY);

    let width = $('#imageSvg').width();
    let height = $('#imageSvg').height();
    let r = parseFloat(val.replace(',','.'));
    let x = (relativeX - (width / 2)) / 70;
    let y = ((height / 2) - relativeY) / 70;

    // document.getElementById('x').value = (x * r).toFixed(2);
    //
    // document.getElementById('yCustom').value = (y * r).toFixed(2);
    // $('input[name=y]').prop('checked',false);
    // $('input[id=yCustom]').prop('checked', true);
    setFieldValue("x", (x * r).toFixed(2));
    setFieldValue("y", (y * r).toFixed(2));

    //$('select option:eq(10)').prop('selected',true)

    // document.getElementById('x').value = (x * r).toFixed(2);
    // document.getElementById('yCustom').value = (y * r).toFixed(2);
    // document.getElementById('yCustom').value = (y * r).toFixed(2);
    // $('input[id=yCustom]').prop('selected', true);

    // $('input[name=y]').prop('checked',false);
    //
    // document.getElementById('form').elements['x'].type;
    // document.getElementById('x').value = (x * r).toFixed(2);
    // //document.getElementById("xValue").value = (x * r).toFixed(2);
    // document.getElementById('yCustom').value = (y * r).toFixed(2);
    // $('input[id=yCustom]').prop('checked', true);

    //console.log(x, y, r);

    $('#form').submit();

}

function setFieldValue(name, value) {
    let custom = name + "Custom";
    if($('#'+name).length) {
        document.getElementById(name).value = value;
        console.log(name, "a");
        if($('#'+custom).length) {
            document.getElementById(name+"Custom").value = value;
            console.log(name, "aa");
            $('input[name=' + name + ']').prop('checked',false);
            $('#' + custom).prop('selected', true);
        }
    } else {
        document.getElementById(name + 'Custom').value = value;
        $('input[name=' + name + ']').prop('checked',false);
        $('#' + custom).prop('checked', true);
        console.log(name, "b");
    }
}

$(document).ready(function () {
    $('#imageSvg').click(imageClicked);
});

// $("#imageSvg").addEventListener("click", printMousePos);