<script>
    import { messageStore } from './store.js'
    import { onDestroy } from 'svelte'
    
    let count = 0
    let notifications = []
    let unsubscribe

    let createNotification = (msg) => {
        notifications = [{
              id: count,
              msg, 
          }, ...notifications];
          count += 1
    }
        
    unsubscribe = messageStore.subscribe(value => {
            if (!value) { return }
            createNotification(value.message)
            messageStore.set()
        })
    onDestroy(unsubscribe)
      
    let removeNotification = (id) => { 
        notifications = notifications.filter(n => n.id != id)
    }
</script>

<ul class="notifications">
	{#each notifications as notification (notification.id)}
		<li class="notification" 
            on:animationend={() => removeNotification(notification.id) }
            on:click={() => removeNotification(notification.id) }>
            {notification.msg}
		</li>	
	{/each}
</ul>

<style>
    ul.notifications {
        position: absolute;
        bottom: 10px;
        right: 20px;
    }

    li.notification {
        text-align: center;
        min-width: 150px;
        padding: 10px;
        margin: 10px;
        font-size: 12px;
        border-radius: 5px;
        background-color: rgb(107, 107, 107);
        color: white;
        cursor: pointer;
        animation-duration: 3s;
        animation-name: notification;
        animation-iteration-count: 1;
    }

@keyframes notification {
    from {bottom:0}
    to {bottom:20px}
}
    
</style>
