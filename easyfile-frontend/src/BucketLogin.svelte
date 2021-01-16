<script>
    import { getToken } from './api.js';
    
    export let bucketId;
    export let token;
    let password;
    let status;

    async function login() {
        let response = await getToken(bucketId, password)
            .catch(() => alert("something went wrong"))
        if (response.status == 200) {
            token = response.token
        }
        status = response.status
    }

</script>
<div id="login">
    <p>Bucket:</p>
    <p><code>{bucketId}</code></p>
    <p>Password:</p>
    <p>
        <input 
            type="password" 
            placeholder="password" 
            bind:value={password} 
            on:keyup|preventDefault={(e) => e.code == 'Enter' ? login() : false}
        />
        <button on:click={login} >Ok</button>
    </p>
</div>
{#if status == 401} 
<p>access denied</p>
{/if}

<style>
#login {
    border-radius: 25px;
    text-align: center;
    width: 50%;
    margin: 0 auto;
    padding: 30px;
    background-color: #e1e1e1;
}
</style>