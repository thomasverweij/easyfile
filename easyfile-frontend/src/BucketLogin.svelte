<script>
    import { getToken } from './api.js';
    
    export let bucketId;
    export let token;
    let password;
    let status;

    async function login() {
        let response = await getToken(bucketId, password)
        if (response.status == 200) {
            token = response.token
        }
        status = response.status
    }

</script>

<p>Password for bucket: {bucketId}</p>
<p>
    <input type="password" placeholder="password" bind:value={password} />
    <button on:click={login} >Ok</button>
</p>
{#if status == 401} 
<p>access denied</p>
{/if}