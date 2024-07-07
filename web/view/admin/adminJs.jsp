<%-- 
    Document   : adminJs
    Created on : Jun 27, 2024, 8:11:36 PM
    Author     : Admin
--%>





<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<div style="height:200px; width:100vw;"></div>
<script>
    function fetchNotifications() {
        fetch('<%=request.getContextPath()%>/fetchNotifications')
                .then(response => response.json())
                .then(data => {
                    console.log('Fetched notifications:', data); // Debugging output
                    let notificationDropdown = document.getElementById('notificationDropdown');
                    notificationDropdown.innerHTML = ''; // Clear previous notifications
                    if (data.length > 0) {
                        data.forEach(notif => {
                            console.log('Notification message:', notif.message); // Log each message
                            if (notif.message) { // Ensure the message is not undefined or null
                                let notificationItem = document.createElement('a');
                                notificationItem.className = 'dropdown-item';
                                notificationItem.href = '<%= request.getContextPath() %>/AMainController?action=orderDetail' + '&orderId=' + notif.id;
                                notificationItem.innerHTML = notif.message + `
            
                                        <button class="btn btn-primary btn-sm">View</button>
                                    `;
                                notificationDropdown.appendChild(notificationItem);
                            }
                        });
                    } else {
                        notificationDropdown.innerHTML = '<a class="dropdown-item" href="#">No new notifications</a>';
                    }
                })
                .catch(error => console.error('Error fetching notifications:', error));
    }



    // Polling every 5 seconds
    setInterval(fetchNotifications, 5000);
</script>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>


