using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
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
    [Route("api2/{token}/doctor")]
    public class DoctorController2 : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly DoctorRepository _doctor;
        private readonly LoginRepository _login;

        public DoctorController2(ApplicationContext context)
        {
            _context = context;
            _doctor = new DoctorRepository(_context);
            _login = new LoginRepository(_context);
        }

        private Login GetLogin(string token, out string error)
        {
            if (!_login.IsTokenExist(token))
            {
                error = "This token is not exist";
                return null;
            }
            error = null;
            return _login.Get(token);
        }

        private static bool IsTokenExpired(DateTime expires)
        {
            if (expires < DateTime.Now)
                return true;
            return false;
        }

        private static bool IsTokenBelongsToDoctor(UserType userType)
        {
            if (userType == UserType.Doctor)
                return true;
            return false;
        }

        [HttpGet]
        public IActionResult Get(string token)
        {
#if RELEASE
            try
            {
#endif
            Login login = GetLogin(token, out string error);
            if (login is null)
                return Conflict(error);

            if (IsTokenExpired(login.Expires))
                return Conflict(Constants.tokenExpired);

            if (!IsTokenBelongsToDoctor(login.UserType))
                return Conflict(Constants.notDoctor);

            if (!_doctor.IsIdExist(login.UserId))
                return Conflict("No such doctor");

            return Ok(new DoctorInfo(_doctor.Get(login.UserId)));
#if RELEASE
            }
            catch (Exception)
            {
                return BadRequest(Constants.defaultExceptionText);
            }
#endif
        }

        [HttpPut("{json}")]
        public IActionResult Edit(string token, string json)
        {
#if RELEASE
            try
            {
#endif
                var item = JsonConvert.DeserializeObject<DoctorFromUser>(json);

                Login login = GetLogin(token, out string error);
                if (login is null)
                    return Conflict(error);

                if (IsTokenExpired(login.Expires))
                    return Conflict(Constants.tokenExpired);

                if (IsTokenBelongsToDoctor(login.UserType))
                    return Conflict(Constants.notPatient);

                if (!_doctor.IsIdExist(login.UserId))
                    return Conflict("No such patient");


                _doctor.Update(new Doctor()
                {
                    Id = login.UserId,
                    FullName = item.FullName
                });

                return Ok(new DoctorInfo(_doctor.Get(login.UserId)));
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
