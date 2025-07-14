const { Telegraf } = require('telegraf')
const bot = new Telegraf('7509442849:AAHO6JwjtkfObj16vaX20sxij0nOzlqYKC4');
const redis = require('redis');

// Redis sozlamalari
const REDIS_HOST = process.env.REDIS_HOST || 'localhost';
const REDIS_PORT = process.env.REDIS_PORT || 6379;
const REDIS_PASSWORD = process.env.REDIS_PASSWORD || 'paybook';

console.log(`Redis host: ${REDIS_HOST}, port: ${REDIS_PORT}, password: ${REDIS_PASSWORD}`);



const redisClient = redis.createClient({
    host: REDIS_HOST,
    port: REDIS_PORT,
    password: REDIS_PASSWORD
});

redisClient.connect()
    .then(() => console.log('Redisga ulanish muvaffaqiyatli amalga oshirildi'))
    .catch(err => console.error('Redisga ulanishda xatolik:', err));




// Botga /start buyrug'i berilganda
bot.start((ctx) => ctx.reply(`
Assalomu alaykum, *${ctx.from.first_name}!*  
Paybook botiga xush kelibsiz! 😊  
Saytimizga kirish uchun 1 daqiqalik maxsus kod olish uchun quyidagi tugma orqali telefon raqamingizni yuboring.
`, {
    parse_mode: 'Markdown',
    reply_markup: {
        keyboard: [
            [
                {
                    text: '📱 Telefon raqamimni yuborish',
                    request_contact: true
                }
            ]
        ],
        resize_keyboard: true,
        one_time_keyboard: true
    }
}));

// Telefon raqami yuborilganda
bot.on('contact', async (ctx) => {
    const contact = ctx.message.contact;
    const phoneNumber = contact.phone_number;

    // Faqat o'z raqamini yuborishga ruxsat berish
    if (contact.user_id !== ctx.from.id) {
        return ctx.reply('⚠️ Iltimos, faqat o‘zingizning telefon raqamingizni yuboring.');
    }

    // Foydalanuvchi ma'lumotlarini Redisga saqlash
    const userData = {
        telegram_id: ctx.from.id,
        phone_number: phoneNumber,
        name: ctx.from.first_name + ' ' + (ctx.from.last_name || '')
    };

    // 6 xonali tasodifiy kod yaratish va uning mavjudligini tekshirish
    let codeExists = true;
    let code = '';
    while (codeExists) {
        code = Math.floor(100000 + Math.random() * 900000).toString();
        const exists = await redisClient.exists(code);
        codeExists = exists === 1;
    }

    // Kodni Redisga 1 daqiqa (60 soniya) muddatga saqlash
    await redisClient.set(code, JSON.stringify(userData), { EX: 60 });

    return ctx.reply(`
🎉 Tabriklaymiz, *${ctx.from.first_name}!*  

Sizning telefon raqamingiz muvaffaqiyatli qabul qilindi!  

Sizga maxsus kod:

\`\`\`Kirish-kodi
${code}\`\`\`  
Bu kod 1 daqiqa davomida amal qiladi. Iltimos, uni paybook.uz saytida yoki mobil ilovada kirish uchun ishlating.  
*Eslatma:* Kodni hech kim bilan ulashmang! 🔒
`, {
        parse_mode: 'Markdown',
        reply_markup: {
            remove_keyboard: true
        }
    });
});

// Botni ishga tushirish
bot.launch();

// Botni muammosiz to'xtatish
process.once('SIGINT', () => bot.stop('SIGINT'));
process.once('SIGTERM', () => bot.stop('SIGTERM'));