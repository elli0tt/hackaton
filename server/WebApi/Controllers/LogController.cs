using Microsoft.AspNetCore.Mvc;
using System;
using System.Linq;
using WebApi.Data;
using WebApi.Enums;
using WebApi.Models;
using WebApi.Models.Support;
using WebApi.Repositories;
using WebApi.Tools;

namespace WebApi.Controllers
{
    [ApiController]
    [Route("api/log/{type}")]
    public class LogController : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly LoginRepository _login;
        private readonly PatientRepository _patient;
        private readonly DoctorRepository _doctor;

        public LogController(ApplicationContext context)
        {
            _context = context;
            _login = new LoginRepository(_context);
            _patient = new PatientRepository(_context);
            _doctor = new DoctorRepository(_context);
        }

        [HttpPost]
        public IActionResult Log(string type, [FromBody] LogPass item)
        {
#if RELEASE
            try
            {
#endif
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

            long id;
            byte[] hashed;
            byte[] hash;

            if (isDoc)
            {
                Doctor doctor = _doctor.Get(item.Username);
                hashed = PswdCrypt.GenerateHash(item.Password, doctor.PasswordSalt);

                id = doctor.Id;
                hash = doctor.PasswordHash;
            }
            else
            {
                Patient patient = _patient.Get(item.Username);
                hashed = PswdCrypt.GenerateHash(item.Password, patient.PasswordSalt);

                id = patient.Id;
                hash = patient.PasswordHash;
            }

            if (!hashed.SequenceEqual(hash))
                return Conflict("Incorrect password");

            UserType userType = isDoc ? UserType.Doctor : UserType.Patient;

            Login login = new Login()
            {
                Id = id,
                UserType = userType,
                Expires = DateTime.Now.AddMinutes(Constants.tokenLifetime),
                Token = Guid.NewGuid()
            };
            if (_login.IsExist(id, userType))
                _login.Delete(id, userType);

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
