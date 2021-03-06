using Microsoft.AspNetCore.Mvc;
using System;
using System.Linq;
using WebApi.Data;
using WebApi.Enums;
using WebApi.Models;
using WebApi.Models.Support;
using WebApi.Repositories;
using WebApi.Tools;
using Newtonsoft.Json;

namespace WebApi.Controllers
{
    [ApiController]
    [Route("api2/log/{type}")]
    public class LogController2 : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly LoginRepository _login;
        private readonly PatientRepository _patient;
        private readonly DoctorRepository _doctor;

        public LogController2(ApplicationContext context)
        {
            _context = context;
            _login = new LoginRepository(_context);
            _patient = new PatientRepository(_context);
            _doctor = new DoctorRepository(_context);
        }

        [HttpPost("{json}")]
        public IActionResult Log(string type, string json)
        {
#if RELEASE
            try
            {
#endif
            var item = JsonConvert.DeserializeObject<LogPass>(json);

            bool isDoc;
            if (type == "doc")
                isDoc = true;
            else if (type == "pat")
                isDoc = false;
            else
                return BadRequest("Unknown type");

            if (item == null || !ModelState.IsValid)
                return BadRequest(Constants.bodyItemIncorrect);

            bool usernameExists;
            if (isDoc)
                usernameExists = _doctor.IsUsernameExist(item.Username);
            else
                usernameExists = _patient.IsUsernameExist(item.Username);

            if (!usernameExists)
                return Conflict("Username does not exist");

            long userId;
            byte[] hashed;
            byte[] hash;

            if (isDoc)
            {
                Doctor doctor = _doctor.Get(item.Username);
                hashed = PswdCrypt.GenerateHash(item.Password, doctor.PasswordSalt);

                userId = doctor.Id;
                hash = doctor.PasswordHash;
            }
            else
            {
                Patient patient = _patient.Get(item.Username);
                hashed = PswdCrypt.GenerateHash(item.Password, patient.PasswordSalt);

                userId = patient.Id;
                hash = patient.PasswordHash;
            }

            if (!hashed.SequenceEqual(hash))
                return Conflict("Incorrect password");

            UserType userType = isDoc ? UserType.Doctor : UserType.Patient;

            Login login = new Login()
            {
                UserId = userId,
                UserType = userType,
                Expires = DateTime.Now.AddMinutes(Constants.tokenLifetime),
                Token = Guid.NewGuid()
            };
            if (_login.IsExist(userId, userType))
            {
                login.Id = _login.Get(userId, userType).Id;
                return Ok(_login.Update(login).Token.ToString());
            }

            return Ok(_login.Insert(login).Token.ToString());
#if RELEASE
            }
            catch (Exception)
            {
                return BadRequest(Constants.defaultExceptionText);
            }
#endif
        }
    }
}
