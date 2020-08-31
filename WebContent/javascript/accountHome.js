$(document).ready(function () {
	$.ajax({url: 'Authorise',
		type: 'GET',
		success: function(data, textStatus, request) {
			if(request.getResponseHeader('Admin')) {
				$('#create-room-container').show();
			}
		},
		error: function(error) {
			window.location.href = 'index.html';
		}
	})
});

$(document).ready(function() {
	$('#date-display').html(d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate());
	getTableData();
});

var d = new Date();

function incrementDate() {
	d.setDate(d.getDate() + 1);
	$('#date-display').html(d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate());
	getTableData();
}

function decrementDate() {
	d.setDate(d.getDate() - 1);
	$('#date-display').html(d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate());
	getTableData();
}

function getTableData() {
	$('#available-bookings-loading').show();
	$('#user-bookings-loading').show();
	
	let dateString = d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
	
	$.ajax({url: 'Bookings?date=' + dateString,
		type: 'GET',
		success: function(data) {
			setupAvailableBookingsTable(data);
			$('#available-bookings-loading').hide();
		},
		error: function(error) {
			console.log(error);
			window.location.href = 'index.html';
		}
	})
		
	$.ajax({url: 'Rooms',
		type: 'GET',
		success: function(data) {
			setupUserBookingsTable(data);
			$('#user-bookings-loading').hide();
		},
		error: function(error) {
			console.log(error);
			window.location.href = 'index.html';
		}
	})
}

function setupUserBookingsTable(data) {
	$('#user-bookings-container').html(data);
	$('.cancelButton').click(function() {
		handleCancelButton(this);
	})
}

function handleCancelButton(button) {
	$('#user-bookings-loading').show();
	let room = $('td:nth-child(1)', $(button).parents('tr')).text();
	let time = $('td:nth-child(3)', $(button).parents('tr')).text();
	cancelRoom(room, time);
}

function setupAvailableBookingsTable(data) {
	$('#available-bookings-container').html(data);
	$('#available-bookings-table td').click(function() {
		handleCellClick(this);
	})
}

function handleCellClick(cell) {
    let col = parseInt($(cell).index());
    
    if (col === 0 || $(cell).html() === 'X') return;
    else {
    	$('#available-bookings-loading').show();
    	let time = $('#available-bookings-table th').eq(col).text();
    	let room = $('td:first', $(cell).parents('tr')).text();
    	bookRoom(room, time);
    }
}

function bookRoom(room, time) {
	let dateString = d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
	
	$.ajax({url: 'Bookings?date=' + dateString + '&room=' + room + '&time=' + time,
		type: 'POST',
		success: function(data) {
			getTableData();
			$('#available-bookings-loading').hide();
		},
		error: function(error) {
			window.location.href = 'index.html';
		}
	})
}

function cancelRoom(room, time) {
	let dateString = d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
	
	$.ajax({url: 'Rooms?date=' + dateString + '&room=' + room + '&time=' + time,
		type: 'DELETE',
		data: room + '-' + time,
		success: function(data) {
			getTableData();
			$('#user-bookings-loading').hide();
		},
		error: function(error) {
			window.location.href = 'index.html';
		}
	})
}

