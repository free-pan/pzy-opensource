-- 按前缀删除redis数据,并返回删除的记录数
--游标的id
local cursor = 0
--查找删除的key的数量
local keyNum = 0
repeat
    --使用scan搜索，cursor=0的时候标识一个新的迭代期,服务器返回0的时候表示迭代已经结束
    local res = redis.call("scan",cursor,"MATCH",KEYS[1])
    if(res ~= nil and #res>=0) then
        cursor = tonumber(res[1])
        local ks = res[2]
        if(ks ~= nil and #ks>0) then
            redis.replicate_commands()
            --循环删除当前迭代器迭代出的数据
            for i=1,#ks,1 do
                local key = tostring(ks[i])
                --使用UNLINK删除，区别于del的是这个是异步执行的
                --这条指令要版本大于4.0.0 小于4.0.0就使用del
                redis.call("UNLINK",key)
            end
            --统计删除的key的数量
            keyNum = keyNum + #ks
        end
    end
--当服务器返回0的时候，跳出循环
until( cursor <= 0 )

return keyNum