using Microsoft.AspNetCore.Mvc;
using System;
using WebApi.Data;
using WebApi.Enums;
using WebApi.Models;
using WebApi.Models.Support;
using WebApi.Repositories;
using WebApi.Tools;

namespace WebApi.Controllers
{
    [ApiController]
    [Route("api/reg/{type}")]
    public class RegController : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly LoginRepository _login;
        private readonly PatientRepository _patient;
        private readonly DoctorRepository _doctor;

        public RegController(ApplicationContext context)
        {
            _context = context;
            _login = new LoginRepository(_context);
            _patient = new PatientRepository(_context);
            _doctor = new DoctorRepository(_context);
        }

        [HttpPost]
        public IActionResult Reg(string type, [FromBody] LogPass item)
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

                bool usernameExists = false;
                if (isDoc)
                    usernameExists = _doctor.IsUsernameExist(item.Username);
                else
                    usernameExists = _patient.IsUsernameExist(item.Username);

                if (usernameExists)
                    return Conflict("Username already exists");

                byte[] salt = PswdCrypt.GenerateSalt();
                byte[] hashed = PswdCrypt.GenerateHash(item.Password, salt);

                long id = 0;

                if (isDoc)
                {
                    id = _doctor.Create(new Doctor()
                    {
                        Username = item.Username,
                        PasswordSalt = salt,
                        PasswordHash = hashed,
                        DateOfReg = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond
                    })
                        .Id;
                }
                else
                {
                    id = _patient.Create(new Patient()
                    {
                        Username = item.Username,
                        PasswordSalt = salt,
                        PasswordHash = hashed,
                        DateOfReg = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond,
                        SNILS = item.SNILS
                    })
                        .Id;
                }

                UserType userType = isDoc ? UserType.Doctor : UserType.Patient;

                Login login = new Login()
                {
                    UserId = id,
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
